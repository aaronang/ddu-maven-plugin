package pt.up.fe.ddu.base.instrumentation.matchers;

import java.security.ProtectionDomain;

import javassist.CtClass;
import javassist.CtMethod;
import pt.up.fe.ddu.base.runtime.Collector;

public class DuplicateCollectorReferenceMatcher implements Matcher {

	@Override
	public boolean matches(CtClass c, ProtectionDomain d) {
		return Collector.instance().existsHitVector(c.getName());
	}

	@Override
	public boolean matches(CtClass c, CtMethod m) {
		return matches(c, (ProtectionDomain) null);
	}

}
