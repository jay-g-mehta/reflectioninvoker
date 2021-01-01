package reflectioninvoker.targets.config;

import reflectioninvoker.targets.Target;

import java.util.List;

/**
 * Translator to interpret Target config definition and translate to Target objects.
 */
public interface TargetsConfigTranslator {
    List<Target> translate(final String config);
}
