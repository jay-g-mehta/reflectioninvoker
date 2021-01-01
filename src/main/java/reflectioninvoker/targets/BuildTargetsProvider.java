package reflectioninvoker.targets;

import lombok.Builder;
import lombok.Singular;

import java.util.List;

/**
 * This TargetsProvider exposes methods to chain targets in any sequence. This can be used for dynamically
 * building Targets and chaining at runtime.
 */
@Builder
public class BuildTargetsProvider implements TargetsProvider{

    @Singular
    private List<Target> targets;

    @Override
    public List<Target> get() {
        return this.targets;
    }
}
