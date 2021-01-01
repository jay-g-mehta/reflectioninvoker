package reflectioninvoker.targets;

import java.util.List;

/**
 * Interface for implementors to adhere, in providing a list of Target. Implementors can choose to build list of Target,
 * read config and build list of Target, etc.
 */
public interface TargetsProvider {
    List<Target> get();
}
