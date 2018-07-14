package org.bakasoft.framboyan.junit;

import java.util.HashMap;

import org.bakasoft.framboyan.Node;
import org.bakasoft.framboyan.Target;
import org.junit.runner.Description;

public class TargetDescriptor {

	private final HashMap<Node, Description> nodeDescriptions;
	
	private final Node rootNode;
	private final Description rootDescription;
	
	public TargetDescriptor(Target target) {
		nodeDescriptions = new HashMap<>();
		rootNode = target.buildInto(null);
		rootDescription = createSuite(rootNode, target.getClass());
	}
	
	private Description createSuite(Node suiteNode, Class<?> suiteClass) {
		Description suite;
		
		if (suiteNode.hasDescription()) {
			suite = Description.createSuiteDescription(suiteNode.getStringDescription());
		}
		else {
			suite = Description.createSuiteDescription(suiteClass);
		}
		
		for (Node child : suiteNode.getChildren()) {
			createTests(suite, suiteNode, child, suiteClass);
		}
		
		return suite;
	}
	
	private void createTests(Description suite, Node root, Node node, Class<?> rootClass) {
		Target target = node.getTarget();
		
		if (node.getAction() == null && target != null && target.isRoot()) {
			Description subsuite = createSuite(node, target.getClass());
			
			suite.addChild(subsuite);
		}
		else {
			for (Node child : node.findNodes(child -> child.getAction() != null)) {
				String testName = child.joinDescriptions(root);
				Description test = Description.createTestDescription(rootClass, testName);
				
				suite.addChild(test);
				
				nodeDescriptions.put(child, test);
			}
		}
	}

	public Description getRootDescription() {
		return rootDescription;
	}

	public Node getRootNode() {
		return rootNode;
	}

	public Description getDescription(Node node) {
		return nodeDescriptions.get(node);
	}

}
