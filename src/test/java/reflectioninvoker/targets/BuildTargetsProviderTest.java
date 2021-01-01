package reflectioninvoker.targets;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuildTargetsProviderTest {

    @Test
    void should_construct_and_get() {
        Target targetInstance = new Target();
        BuildTargetsProvider buildTargetsProvider = BuildTargetsProvider.builder()
                .target(targetInstance)
                .build();
        assertEquals(targetInstance, buildTargetsProvider.get().get(0));
    }
}