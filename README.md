Bus Ticket Booking System – Full Stack Web App
Spring Boot · Spring Data JPA / Hibernate · MySQL · Maven · HTML · CSS

• Architected a comprehensive Bus Ticket Booking application using Java, Spring Boot, and MVC architecture, designing and exposing 20+ 
RESTful endpoints to seamlessly manage Bus, Passenger, and Booking modules.

• Modeled complex relational database schemas using Spring Data JPA and Hibernate, configuring optimal @ManyToOne and @OneToMany 
entity mappings while strategically applying @JsonIgnore to prevent infinite recursion and stack overflow errors during API data serialization.

• Automated critical transaction lifecycles by leveraging JPA @PrePersist hooks to dynamically generate unique PNR tracking numbers, register 
booking timestamps, and automatically synchronize real-time bus seat inventory upon entity creation.

• Enforced strict data integrity and security at the application layer using Jakarta Bean Validation (including Regex patterns and min/max 
constraints), paired with a global exception handler to intercept invalid inputs and return sanitized, custom HTTP error responses.

• Developed a server-side rendered frontend using Thymeleaf, HTML, and CSS, optimizing database query performance and application memory 
by utilizing FetchType.LAZY to load associated entity data only when explicitly required by the UI.
