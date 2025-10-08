# VCRTS – Vehicular Cloud Real-Time System

## Introduction
The Vehicular Cloud Real-Time System (VCRTS) is designed to manage and allocate computational resources in a vehicular cloud environment.  

Vehicle owners can contribute the unused computational capacity of their vehicles, while job owners can submit computational tasks that will be executed by the available vehicles. The system also provides real-time tracking of tasks and a dashboard for monitoring progress .

---

## Objectives
- Provide a platform for registering vehicles and adding them to a shared resource pool  
- Allow job owners to submit computational tasks with constraints such as deadlines and duration  
- Manage allocation of resources through the Vehicular Cloud Controller  
- Display real-time reports of jobs and resource usage  
- Scale to support hundreds of simultaneous users  
- Ensure system responses within one second  

---

## User Roles
- **Vehicle Owner**: Registers vehicles, provides specifications (make, model, year), and contributes them to the resource pool  
- **Job Owner**: Submits computational tasks with duration and deadline requirements  
- **Vehicular Cloud Controller**: Manages resources, performs allocation, and oversees operations  

All users must create accounts, and both job and vehicle owners interact with the system through the Controller.

---

## Functional Requirements
- **Vehicle Management**: Register vehicles and add them to the computational pool  
- **Job Management**: Register job owners, submit jobs, and keep jobs until completion  
- **Resource Allocation**: Automatically match jobs to vehicles using allocation algorithms  
- **Monitoring**: Display job status (pending, in-progress, completed), show resource usage, and update dashboards in under one second  
- **Account Management**: User login, logout, and account handling  

---

## Non-Functional Requirements
- **Performance**: System responses must be ≤ 1 second  
- **Compatibility**: Support Windows 10/11 (64-bit) and macOS  
- **Security**: Store passwords with secure hashing (e.g., SHA-256); automatically delete job data within 24 hours after completion  
- **Reliability**: Save jobs in persistent storage every 30 seconds; recover from crashes within 5 seconds  
- **Size Constraints**: Operate within ≤ 1GB RAM while supporting at least 500 concurrent users  
- **Ease of Use**: Provide help and documentation with at least five contextual help frames  

---

## Software Evolution
- **Current Version**: Local Java desktop application with GUI panels for Controller, Vehicle Owner, and Job Owner  
- **Future Development**: Expansion into a fully web-based platform accessible remotely   

---

## Getting Started
1. Clone or download the repository  
2. Open the project in a Java IDE  
3. Run App.java to start

---

## Notes
- Vehicle and job owners do not communicate directly; all coordination is managed by the Controller  
- The system is intended to scale and evolve into a distributed, web-based environment  
