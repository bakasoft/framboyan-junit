package org.bakasoft.framboyan.junit;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.RunWith;

import org.bakasoft.framboyan.FramboyanLoader;
import org.bakasoft.framboyan.Node;
import org.bakasoft.framboyan.Target;
import org.bakasoft.framboyan.util.Reflection;

@RunWith(FramboyanJUnitRunner.class)
abstract public class FramboyanJUnit implements Target {

	private final ArrayList<Target> targets;
	private final ArrayList<Class<? extends Target>> ignored;
	
	private boolean findTargets;
	
	private boolean autoAddFlag;
	
	public FramboyanJUnit() {
		targets = new ArrayList<>();
		ignored = new ArrayList<>();
		findTargets = true;
		autoAddFlag = true;
	}
	
	@Override
	public boolean isRoot() {
		return false;
	}

	@Override
	public Node buildInto(Node parent) {
		Node node = new Node(parent, this, getDescription(), null, isPending(), isFocused(), null);
		
		for (Target target : getTargets()) {
			Node subnode = target.buildInto(node);
			
			node.addNode(subnode);
		}
		
		return node;
	}
	
	public void ignore(Class<? extends Target> clazz) {
		ignored.add(clazz);
	}
	
	public void add(Class<? extends Target> targetType) {
		Target target = Reflection.createInstance(targetType);
		
		add(target);
	}
	
	public void add(Target target) {
		// avoid circular references
		if (FramboyanJUnit.class.isInstance(target)) {
			throw new RuntimeException();
		}

		targets.add(target);
	}
	
	@Override
	public List<Target> getTargets() {
		if (findTargets && autoAddFlag) {
			for (Target target : FramboyanLoader.findTargets()) {
				// avoid circular references
				if (isAllowedClass(target.getClass())) {
					targets.add(target);		
				}
			}
			
			autoAddFlag = false;
		}
		
		return targets;
	}
	
	private boolean isAllowedClass(Class<? extends Target> clazz) {
		return !FramboyanJUnit.class.isAssignableFrom(clazz) 
				&& !ignored.stream().anyMatch(ignoredClass -> ignoredClass.isAssignableFrom(clazz));
	}

	@Override
	public Object getDescription() {
		return Target.getDescriptionFromClass(getClass(), null);
	}

	@Override
	public boolean isPending() {
		return Target.getPendingFromClass(getClass(), false);
	}

	@Override
	public boolean isFocused() {
		return Target.getFocusedFromClass(getClass(), false);
	}
	
}
