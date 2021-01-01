package reflectioninvoker.targets;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reflectioninvoker.targets.config.TargetsConfigReader;
import reflectioninvoker.targets.config.TargetsConfigTranslator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ConfigTargetsProviderTest {

    ConfigTargetsProvider configTargetsProvider;
    @Mock
    TargetsConfigReader targetsConfigReader;
    @Mock
    TargetsConfigTranslator targetsConfigTranslator;

    @Test
    void should_build_and_get() {
        configTargetsProvider = new ConfigTargetsProvider(targetsConfigReader, targetsConfigTranslator);
        String dummy = "SOME STRING";
        Target target = new Target();
        Mockito.when(targetsConfigReader.read()).thenReturn(dummy);
        Mockito.when(targetsConfigTranslator.translate(dummy)).thenReturn(ImmutableList.of(target));
        configTargetsProvider.build();
        assertEquals(target, configTargetsProvider.get().get(0));
    }
}