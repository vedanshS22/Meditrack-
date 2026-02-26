# Java Setup Instructions for MediTrack

## 1. Install JDK

- **Download JDK**: Install a recent Long Term Support (LTS) JDK (e.g., JDK 17) from a trusted source such as:
  - Oracle
  - Eclipse Temurin (Adoptium)
  - OpenJDK builds
- **Verify installation**:
  - Open a terminal (Command Prompt or PowerShell).
  - Run:
    - `java -version`
    - `javac -version`
  - Both commands should display the installed JDK version.

> Screenshot 1: Output of `java -version` and `javac -version`.

## 2. Configure Environment Variables (Windows)

- Add the JDK `bin` directory to the `PATH`:
  - Open **System Properties → Advanced → Environment Variables**.
  - Under **System variables**, locate `Path` and click **Edit**.
  - Add a new entry pointing to your JDK `bin` folder, e.g.:
    - `C:\Program Files\Java\jdk-17\bin`
- Optionally set `JAVA_HOME`:
  - Create a new system variable:
    - **Name**: `JAVA_HOME`
    - **Value**: JDK installation directory (e.g. `C:\Program Files\Java\jdk-17`)

> Screenshot 2: Environment variables showing `JAVA_HOME` and updated `Path`.

## 3. Verify JDK on Command Line

- Open a new terminal window to ensure updated `PATH` is loaded.
- Run a simple Java program:
  1. Create a file `Hello.java`:

     ```java
     public class Hello {
         public static void main(String[] args) {
             System.out.println("Hello, MediTrack!");
         }
     }
     ```

  2. Compile and run:
     - `javac Hello.java`
     - `java Hello`

> Screenshot 3: Compilation and execution of `Hello` program.

## 4. Maven (Optional but Recommended)

- Install Apache Maven from the official website.
- Add the Maven `bin` directory to `PATH`.
- Verify installation with:
  - `mvn -version`

> Screenshot 4: Output of `mvn -version`.

## 5. Running MediTrack

- Clone or download the MediTrack project.
- From the project root:
  - Compile with Maven:
    - `mvn clean compile`
  - Or compile with `javac`:
    - `javac -d out $(Get-ChildItem -Recurse -Filter *.java | ForEach-Object { $_.FullName })`
- Run the main application:
  - `java -cp target/classes com.airtribe.meditrack.Main`
  - Or using the compiled `out` folder (if you used `javac` directly).

> Screenshot 5: Running `Main` with the menu-driven console UI.

