import java.io.IOException;
public class ForkExecWait {
public static void main(String[] args) {
System.out.println("Parent process: PID = "
+ ProcessHandle.current().pid());
try {
ProcessBuilder pb =
new ProcessBuilder("ls", "-l");
pb.inheritIO();
Process child = pb.start();
System.out.println("Parent waiting for child (PID = "
+ child.pid() + ") to finish...");
System.out.println("Child process: PID = "
+ child.pid());
  System.out.println("Child is executing 'ls -l'");
int status = child.waitFor();
System.out.println("Child exited with status "
+ status);
System.out.println(
"Parent process resumes and exits.");
} catch (IOException e) {
System.out.println("Process execution failed: "
+ e.getMessage());
} catch (InterruptedException e) {
System.out.println("Process interrupted.");
Thread.currentThread().interrupt();
}
}
}
