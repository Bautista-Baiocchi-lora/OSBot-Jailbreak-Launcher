import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

import java.io.File;
import java.util.List;

public class AgentLoader {

    public static void main(String[] args) {
        String agentFilePath = System.getProperty("user.home") + "/Desktop/CustomAgent.jar";
        String jvmAppName = "BotApplication";
        String jvmPid = null;
        List<VirtualMachineDescriptor> jvms = VirtualMachine.list();
        for (VirtualMachineDescriptor jvm : jvms) {
            System.out.println("PID: "+jvm.id() + " - "+jvm.displayName());
                if (jvm.displayName().contains(jvmAppName)) {
                    jvmPid = jvm.id();
                }

        }
        if (jvmPid != null) {
            File agentFile = new File(agentFilePath);
            if (agentFile.isFile()) {
                String agentFileName = agentFile.getName();
                String agentFileExtension = agentFileName.substring(agentFileName.lastIndexOf(".") + 1);
                if (agentFileExtension.equalsIgnoreCase("jar")) {
                    try {
                        System.out.println("Attaching to target JVM with PID: " + jvmPid);
                        VirtualMachine jvm = VirtualMachine.attach(jvmPid);
                        jvm.loadAgent(agentFile.getAbsolutePath()); //attaching.
                        jvm.detach();
                        System.out.println("Attached to target JVM and loaded Java agent successfully");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } else {
            System.out.println("Target JVM not found");
        }
    }
}

