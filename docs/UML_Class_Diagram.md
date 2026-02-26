# MediTrack UML Class Diagram (High Level)

This document captures the main domain classes and relationships for the MediTrack
clinic and appointment management system.

## 1. Package Overview

- **`com.airtribe.meditrack.entity`**: core domain model (persons, doctors, patients, appointments, billing).
- **`com.airtribe.meditrack.service`**: application services (CRUD, search, workflows).
- **`com.airtribe.meditrack.util`**: infrastructure utilities (ID generation, validation, CSV I/O, AI helper).
- **`com.airtribe.meditrack.interfacepkg`**: small, focused interfaces.
- **`com.airtribe.meditrack.exception`**: custom checked/unchecked exceptions.
- **`com.airtribe.meditrack.constants`**: configuration constants.

## 2. Core Domain Diagram (Textual)

```text
        +--------------------+
        |  MedicalEntity     |  <<abstract>>
        |--------------------|
        | - id : String      |
        +--------------------+
                 ^
                 |
        +--------------------+
        |     Person         |  <<abstract>>
        |--------------------|
        | - name : String    |
        | - age  : int       |
        | - phone: String    |
        +--------------------+
          ^              ^
          |              |
 +---------------+   +----------------+
 |   Doctor      |   |    Patient     |
 |---------------|   |----------------|
 | - specialization | | - medicalHistory: List<String> |
 | - fee : double   | +----------------+
 +---------------+        ^
                           (implements Cloneable)

 +----------------------------+
 |   Appointment              |
 |----------------------------|
 | - id : String              |
 | - patient : Patient        |
 | - doctor  : Doctor         |
 | - dateTime : LocalDateTime |
 | - status   : AppointmentStatus (enum)
 +----------------------------+

 +----------------------------+       +----------------------+
 |          Bill             |<------|     BillFactory      |
 |---------------------------|       +----------------------+
 | - id : String             |               ^
 | - appointment : Appointment|              |
 | - baseAmount : double     |              |
 | - billingStrategy :       |      +---------------------------+
 |        BillingStrategy    |<-----|   BillingStrategy (intf)  |
 +---------------------------+      +---------------------------+
       ^ implements Payable          ^                    ^
       |                             |                    |
       |                    +----------------+   +-----------------------+
       |                    |StandardBilling |   | DiscountBilling       |
       |                    |Strategy        |   |Strategy               |
       |                    +----------------+   +-----------------------+

 +-----------------------+
 | BillSummary (immutable)|
 |-----------------------|
 | + billId : String     |
 | + totalAmount: double |
 +-----------------------+
```

## 3. Service Layer Diagram

```text
 +------------------------+      uses       +------------------------+
 |   PatientService       |<--------------->|   DataStore<Patient>   |
 |------------------------|                 +------------------------+
 | + createPatient(...)   |
 | + searchPatient(...)   |
 | + handlePatientMenu()  |
 +------------------------+
             ^
             | implements
             |
      +----------------+
      | Searchable<T>  |
      +----------------+

Similar relationships hold for:

 +------------------------+      +------------------------+
 |   DoctorService        |----->|   DataStore<Doctor>    |
 +------------------------+      +------------------------+

 +-----------------------------+  uses  +--------------------------+
 |    AppointmentService       |------->| DataStore<Appointment>   |
 |-----------------------------|        +--------------------------+
 | + createAppointment(...)    |
 | + cancelAppointment(...)    |
 | + generateBill(...)         |
 +-----------------------------+

All services depend on:

 +----------------------+
 |   IdGenerator (Singleton)
 +----------------------+
```

## 4. Utility and Interface Diagram

```text
 +-------------------+      +-------------------------+
 |   Validator       |      |   CSVUtil               |
 +-------------------+      +-------------------------+
 | static validation |      | save/load CSV data      |
 +-------------------+      +-------------------------+

 +-------------------+      +-------------------------+
 |   AIHelper        |      |   DateUtil              |
 +-------------------+      +-------------------------+
 | recommend doctors |      | parse/format dates      |
 +-------------------+      +-------------------------+

 +-------------------+
 |   Payable         |
 +-------------------+
 | + calculateTotal()|
 +-------------------+

 +-------------------+
 |  Searchable<T>    |
 +-------------------+
 | + findAll()       |
 | + search(...)     |
 +-------------------+
```

If you want to generate a visual UML diagram (PNG/SVG) you can paste the
above structure into a PlantUML editor or use IntelliJ/Cursor plugins
to reverse-engineer the model from the Java sources.

