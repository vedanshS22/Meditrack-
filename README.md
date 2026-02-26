## MediTrack – Clinic & Appointment Management System

MediTrack is a **Core Java**, **object‑oriented** clinic management system that models
patients, doctors, appointments, and billing. It is designed as a **learning project**
to practice **OOP**, **SOLID principles**, **design patterns**, **collections**, **I/O**,
and basic **Java 8+ streams** in a realistic, console‑based application.

The project is intentionally structured like a small real‑world backend service so you
can explore **architecture**, **layering**, and **documentation** (JavaDoc, UML, reports).

---

### 1. Project Layout

- **Base package**: `com.airtribe.meditrack`
- **Source layout**:
  - `src/main/java/com/airtribe/meditrack/`
    - `Main.java` – menu‑driven console UI.
    - `constants/Constants.java` – tax rates, file paths.
    - `entity/` – domain model:
      - `MedicalEntity` (abstract), `Person` (abstract), `Doctor`, `Patient`
      - `Appointment`, `AppointmentStatus` (enum)
      - `Bill`, `BillSummary` (immutable)
      - `BillingStrategy`, `StandardBillingStrategy`, `DiscountBillingStrategy`
      - `BillFactory`, `Specialization` (enum)
    - `service/`
      - `PatientService`, `DoctorService`, `AppointmentService`
    - `util/`
      - `Validator`, `DateUtil`, `CSVUtil`, `IdGenerator`, `DataStore<T>`, `AIHelper`
    - `exception/`
      - `AppointmentNotFoundException`, `InvalidDataException`
    - `interfacepkg/`
      - `Searchable<T>`, `Payable`
  - `src/test/java/com/airtribe/meditrack/TestRunner.java` – manual test harness.
  - `docs/`
    - `Setup_Instructions.md` – JDK/JVM setup screenshots/instructions.
    - `JVM_Report.md` – JVM internals: class loader, heap/stack, JIT, etc.
    - `UML_Class_Diagram.md` – textual UML of the core model.
    - (Optionally) `javadoc/` – generated JavaDoc HTML (see Section 7).

---

### 2. How to Run the App and TestRunner

- **Main console UI**:
  - Compile (from project root, PowerShell):
    ```powershell
    Remove-Item -ErrorAction SilentlyContinue sources.txt
    Get-ChildItem -Recurse -Filter *.java | ForEach-Object { $_.FullName } | Set-Content sources.txt
    javac -cp . -d out (Get-Content sources.txt)
    ```
  - Run the menu‑driven app:
    ```powershell
    java -cp out com.airtribe.meditrack.Main
    ```
  - Optional: load CSV data on startup:
    ```powershell
    java -cp out com.airtribe.meditrack.Main --loadData
    ```

- **Manual `TestRunner`** (advanced OOP & patterns demo):
  ```powershell
  java -cp out com.airtribe.meditrack.TestRunner
  ```
  This demonstrates:
  - Creating doctor, patient, and appointment entities.
  - **Deep cloning** of `Patient` (medical history list is copied).
  - **Strategy + Factory** based billing (standard vs discounted bill totals).

---

### 3. OOP Concepts – What, Why, Where

- **Encapsulation**
  - **What**: Keep fields private and expose behavior through methods.
  - **Where**: `Person`, `Doctor`, `Patient`, `Appointment`, `Bill`, `BillSummary`.
  - **Why**: Prevents invalid state and centralizes validation
    (e.g., creation goes through services + `Validator` instead of scattered checks).

- **Inheritance**
  - **What**: Code reuse via base types.
  - **Where**:
    - `MedicalEntity` → `Person` → `Doctor` / `Patient`
  - **Why**: All domain entities have a stable identity (`id`) and consistent
    `equals`/`hashCode`, implemented once in `MedicalEntity`.

- **Polymorphism**
  - **What**: Same method name, different behavior or signatures.
  - **Where**:
    - Overloading: `PatientService.searchPatient(String id)`, `searchPatient(int age)`,
      `searchPatientByName(String)`.
    - Overriding + dynamic dispatch: `Payable.calculateTotalAmount()` implemented by `Bill`,
      which internally uses different `BillingStrategy` implementations.
  - **Why**: Flexible searching and billing without giant `if/else` blocks.

- **Abstraction & Interfaces**
  - **What**: Hide implementation details behind contracts.
  - **Where**:
    - `Searchable<T>` – generic search interface used by `PatientService` and `DoctorService`.
    - `Payable` – anything that can produce a bill summary.
    - `BillingStrategy` – pricing policies for `Bill`.
  - **Why**: Makes services and billing code easier to extend and test.

- **Advanced OOP**
  - **Cloning (deep copy)**:
    - `Patient` implements `Cloneable` and copies its `medicalHistory` list so original and clone diverge safely.
  - **Immutability**:
    - `BillSummary` is `final`, has only getters, and no setters; once created, its data cannot change.
  - **Enums**:
    - `Specialization`, `AppointmentStatus` provide type‑safe values instead of magic strings.
  - **Static initialization**:
    - `IdGenerator` has a static block to demonstrate one‑time JVM initialization.

---

### 4. SOLID Principles – Why / What / Where

- **S – Single Responsibility Principle (SRP)**
  - **What**: Each class should have one reason to change.
  - **Where**:
    - `PatientService` – only coordinates patient operations (CRUD/search/menu),
      relying on `DataStore` for persistence.
    - `DoctorService`, `AppointmentService` – similar focused responsibilities.
    - `Validator` – central place for input validation.
    - `CSVUtil` – only handles CSV I/O for entities.
  - **Why**: Changes in persistence, validation, or UI do not require editing domain objects.

- **O – Open/Closed Principle (OCP)**
  - **What**: Open for extension, closed for modification.
  - **Where**:
    - `Bill` delegates total calculation to `BillingStrategy`. To add new pricing rules
      (e.g., weekend surcharges, membership discounts), you add new strategy classes
      instead of touching `Bill`.
    - `Searchable<T>.search(Predicate<T>)` uses lambdas; you can add new search types
      by passing different `Predicate` implementations without modifying the interface.
  - **Why**: New behavior through extension, not risky rewrites of existing code.

- **L – Liskov Substitution Principle (LSP)**
  - **What**: Subtypes must be usable wherever their base type is expected.
  - **Where**:
    - `Doctor` and `Patient` behave correctly anywhere a `Person` or `MedicalEntity` is used
      (e.g., collection of `Person`).
  - **Why**: Allows polymorphic collections and future extensions (e.g., `Staff`) without surprises.

- **I – Interface Segregation Principle (ISP)**
  - **What**: Many small interfaces are better than large “fat” ones.
  - **Where**:
    - `Searchable<T>` – only search responsibilities.
    - `Payable` – only billing responsibilities.
    - `BillingStrategy` – only pricing algorithm.
  - **Why**: Classes implement exactly what they need, avoiding empty methods and tight coupling.

- **D – Dependency Inversion Principle (DIP)**
  - **What**: High‑level modules should depend on abstractions, not concretions.
  - **Where**:
    - Services (`PatientService`, `DoctorService`, `AppointmentService`) depend on `DataStore<T>`,
      not a specific `HashMap` or list implementation.
    - `AppointmentService` depends on the abstraction `BillFactory` / `BillingStrategy`
      instead of hardcoding tax/discount formulas.
  - **Why**: You can swap `DataStore` with a DB‑backed implementation or change billing rules
    without editing service code.

---

### 5. Design Patterns – Why / What / Where

- **Singleton – `IdGenerator`**
  - **What**: Single globally accessible, thread‑safe generator using `AtomicInteger`.
  - **Where**: `util/IdGenerator.java` with `getInstance()` and `next*Id()` methods.
  - **Why**: Ensures unique IDs across services without passing counters everywhere.

- **Strategy – `BillingStrategy` and its implementations**
  - **What**: Encapsulate interchangeable algorithms for billing.
  - **Where**:
    - `BillingStrategy` (interface)
    - `StandardBillingStrategy` – tax on base amount.
    - `DiscountBillingStrategy` – discount then tax.
    - `Bill` – holds a `BillingStrategy` and calls `calculateTotal()`.
  - **Why**: Easy to plug in new billing rules (e.g., membership tiers) with new strategy classes.

- **Factory – `BillFactory`**
  - **What**: Central point to build different `Bill` variants.
  - **Where**:
    - `BillFactory.standardBill(...)`
    - `BillFactory.discountedBill(..., discountRate)`
    - Used from `AppointmentService.generateBill(...)`.
  - **Why**: Keeps object creation logic in one place and hides strategy selection from callers.

*(The current version intentionally keeps Template / Observer optional to keep scope manageable.)*

---

### 6. Data Persistence, Streams, and AI Helper

- **CSV Persistence (`CSVUtil`)**
  - **What**: Save/load `Patient`, `Doctor`, `Appointment` to/from CSV files in `data/`.
  - **How**: Uses `String.split(",")` with a simple escape mechanism and `try-with-resources`
    (`Files.newBufferedReader/newBufferedWriter`).
  - **Where**: `CSVUtil.save*` and `CSVUtil.loadInitialData` (invoked when `--loadData` is passed).

- **Collections & Streams**
  - `PatientService` and `DoctorService` use `ArrayList` + `streams` for filtering,
    e.g., search by age, name, specialization.
  - `DoctorService.feeStatistics()` uses stream collectors to compute min/max/avg fees.
  - `AppointmentService.getAppointmentsPerDoctor()` uses `Collectors.groupingBy` for analytics.

- **AI Helper (`AIHelper`)**
  - Simple **rule‑based** recommendation for doctor specialization based on symptoms,
    plus basic analytics (appointments per doctor).
  - Demonstrates how utility services can call core services (`DoctorService`, `AppointmentService`)
    without mixing UI logic.

---

### 7. JavaDoc HTML – How to Generate

Generated JavaDoc is not committed by default (to keep the repo light), but you
can generate it locally under `docs/javadoc`:

```powershell
cd C:\Users\Webkorps\Downloads\Meditrack
javadoc -d docs/javadoc -sourcepath src/main/java -subpackages com.airtribe.meditrack
```

Then open `docs/javadoc/index.html` in a browser to browse the full API docs.

If `javadoc` is not recognized, ensure the JDK `bin` folder is on your `PATH`
(see `docs/Setup_Instructions.md`).

---

### 8. UML Diagram

A high‑level UML class diagram (textual) is provided in:

- `docs/UML_Class_Diagram.md`

You can paste the structure from that file into a PlantUML editor or any UML
tool to generate a visual diagram (PNG/SVG). This diagram focuses on:

- Core entities and inheritance (`MedicalEntity`, `Person`, `Doctor`, `Patient`)
- Associations (`Appointment` ↔ `Doctor`/`Patient`, `Bill` ↔ `Appointment`)
- Strategy/Factory relationships for billing
- Service layer and repository abstraction (`DataStore<T>`)

This README, combined with the JVM and setup reports, is intended to serve as
a complete **documentation bundle** for evaluating your Java fundamentals,
SOLID understanding, and architectural decisions in MediTrack.

