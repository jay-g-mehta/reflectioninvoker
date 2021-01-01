package reflectioninvoker.targets;


import lombok.RequiredArgsConstructor;
import reflectioninvoker.targets.config.TargetsConfigReader;
import reflectioninvoker.targets.config.TargetsConfigTranslator;

import java.util.List;

/**
 * Build list of Target using TargetsConfigReader and TargetsConfigTranslator
 */
@RequiredArgsConstructor
public class ConfigTargetsProvider implements TargetsProvider{

    private final TargetsConfigReader targetsConfigReader;
    private final TargetsConfigTranslator targetsConfigTranslator;
    private List<Target> targets;

    @Override
    public List<Target> get() {
        return this.targets;
    }

    /**
     * Responsible to build list of Target. Caller should invoke this method
     * before calling get().
     */
    public void build() {
        final String config = targetsConfigReader.read();
        targets = targetsConfigTranslator.translate(config);
    }
}
