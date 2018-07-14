package org.bakasoft.framboyan.junit;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

import org.bakasoft.framboyan.Node;
import org.bakasoft.framboyan.Result;
import org.bakasoft.framboyan.Target;
import org.bakasoft.framboyan.util.Reflection;

public class FramboyanJUnitRunner extends org.junit.runner.Runner {

	private final TargetDescriptor descriptor;
	
	public FramboyanJUnitRunner(Class<?> testClass) {
		if(!Target.class.isAssignableFrom(testClass)) {
			throw new RuntimeException();
		}
		
		Target target = Reflection.createInstance(testClass, Target.class);
		
		descriptor = new TargetDescriptor(target);
	}
	
	@Override
	public Description getDescription() {		
		return descriptor.getRootDescription();
	}

	@Override
	public void run(RunNotifier notifier) {
		new org.bakasoft.framboyan.Runner() {

			@Override
			public void started(Node node) {
				Description description = descriptor.getDescription(node);
				
				notifier.fireTestStarted(description);
			}

			@Override
			public void completed(Node node, Result result) {
				Description description = descriptor.getDescription(node);
				
				if (result.isPending()) {
					notifier.fireTestIgnored(description);
				} 
				else if (!result.isSuccessful()) {
					notifier.fireTestFailure(new Failure(description, result.getError()));
				}
				
				notifier.fireTestFinished(description);
			}

			@Override
			public void summary(boolean overallResult, int totalPassed, int totalPending, int totalFailed) {
				// do nothing
			}
			
		}.run(descriptor.getRootNode());
	}
	
}
