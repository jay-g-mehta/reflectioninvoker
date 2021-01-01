package reflectioninvoker.targets;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Represent target of Reflection invoke
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Target {
    private String clazz;
    private String method;
    private List<Object> methodArgs;
    private List<String> methodArgsType;
    private Object clazzInstance;
}
