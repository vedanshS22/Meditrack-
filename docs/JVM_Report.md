# JVM Report for MediTrack

## 1. Class Loader Subsystem

The **Class Loader** is responsible for loading Java classes into the JVM at runtime. It performs:

- **Loading**: Reads `.class` files from the file system or other sources.
- **Linking**:
  - *Verification*: Ensures bytecode is structurally correct and secure.
  - *Preparation*: Allocates memory for static fields and sets default values.
  - *Resolution*: Replaces symbolic references with direct references.
- **Initialization**: Executes static initializers and static blocks.

For MediTrack, the class loader loads classes such as `com.airtribe.meditrack.entity.Patient`, `Doctor`, and service/util classes on demand.

## 2. Runtime Data Areas

The JVM organizes memory into several **Runtime Data Areas**:

- **Method Area**:
  - Stores class-level information: runtime constant pool, field and method data, and code for methods.
  - Holds metadata for classes like `Patient`, `Doctor`, `Appointment`, `Bill`, and enums.

- **Heap**:
  - Shared memory for all threads.
  - Stores all object instances (e.g., `Patient`, `Doctor`, `Appointment`, collections like `ArrayList`, `HashMap`).
  - Managed by the Garbage Collector.

- **Java Stack (per thread)**:
  - Contains stack frames for each method call.
  - A frame stores:
    - Local variables
    - Operand stack
    - Frame data (e.g., return addresses)
  - Local primitives (like `int age`) and references (like `Patient patient`) live here.

- **Program Counter (PC) Register (per thread)**:
  - Holds the address of the currently executing JVM instruction for each thread.

- **Native Method Stack**:
  - Used for native (non-Java) method calls, typically via JNI.

## 3. Execution Engine

The **Execution Engine** runs the bytecode loaded into memory:

- **Interpreter**:
  - Reads and executes bytecode instruction by instruction.
  - Simple but slower due to repeated interpretation.

- **Just-In-Time (JIT) Compiler**:
  - Identifies “hot” methods (frequently executed).
  - Compiles these methods into native machine code.
  - Caches compiled code for subsequent direct execution.

- **Garbage Collector (GC)**:
  - Automatically manages object life cycles.
  - Reclaims memory for objects that are no longer reachable.

In MediTrack, frequently used service methods (e.g., search and CRUD operations) may be JIT-compiled for better performance when the application runs for a longer time.

## 4. JIT Compiler vs Interpreter

- **Interpreter**:
  - Pros: Fast startup, good for short-lived programs or rarely executed branches.
  - Cons: Slower steady-state performance due to repeated interpretation of the same bytecode.

- **JIT Compiler**:
  - Pros: Translates frequently executed bytecode into optimized native instructions; excellent steady-state performance.
  - Cons: Compilation overhead; may introduce small pauses during optimization.

Modern JVMs use a hybrid approach: interpret first, then JIT-compile hot spots.

## 5. “Write Once, Run Anywhere”

Java’s **WORA** promise is enabled by:

- Compiling `.java` source files into platform-independent **bytecode** (`.class` files).
- Running this bytecode on any platform that has a compatible JVM.
- The JVM implementation abstracts away OS and hardware differences.

For MediTrack:

- The same compiled JAR can run on Windows, macOS, or Linux without modification.
- Only the underlying JVM implementation changes.

## 6. How This Relates to MediTrack

- **Class loading**: As users work with patients, doctors, appointments, and billing, the corresponding classes are loaded as needed.
- **Heap usage**: All domain objects (patients, doctors, appointments, bills, and collections) are allocated on the heap.
- **Stacks and PC**: Each thread’s method calls (e.g., from the console UI to services and utilities) use their own stack frames, with the PC register tracking the current instruction.
- **JIT optimizations**: Repeated operations such as searches, filtering, and analytics using streams can be optimized at runtime by the JIT, making the system responsive even for larger datasets.

