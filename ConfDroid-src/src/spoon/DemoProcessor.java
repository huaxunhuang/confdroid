package spoon;

import java.util.HashSet;
import java.util.Set;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.ModifierKind;

public class DemoProcessor extends AbstractProcessor<CtMethod<?>> {
    public final Set<CtClass> ctClassSet = new HashSet<>();

    public void process(CtMethod<?> element) {
        if (element.getParent(CtClass.class) == null || element.getModifiers().contains(ModifierKind.ABSTRACT))
            return;
        this.ctClassSet.add(element.getParent(CtClass.class));
    }
}
