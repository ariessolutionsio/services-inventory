<img align="right" alt="Aries MACH Booster" width="300" src="https://www.ariessolutions.io/wp-content/uploads/2024/01/aries-about-us-imagery.png">


# Inventory Service

## Overview
This project is a microservice designed for managing inventory in high-throughput environments. Unlike traditional warehouse inventory systems that include complex logistics and inventory balancing features, this service focuses on maintaining a streamlined dataset to track availability across different inventory locations (referred to as Nodes within the industry). The primary aim is to support online experiences, where rapid and numerous API calls are expected. The inventory availability is dynamically updated to reflect online sales and stock refreshes from physical stores as they happen.

Utilizing Google Cloud Platform's Cloud Spanner, this microservice combines the benefits of relational database structures with the scalability needed to manage extensive datasets efficiently. Written in Java and containerized for Kubernetes, it offers the flexibility for both vertical and horizontal scaling to ensure high availability and performance.

### Key Features
- **High-Throughput Handling:** Optimized for environments with a high number of API calls.
- **Dynamic Inventory Tracking:** Real-time updates on inventory availability, taking into account both online sales and physical store refreshes.
- **Scalable Architecture:** Leveraging GCP Cloud Spanner for unlimited scalability and relational semantics.
- **Containerized Deployment:** Ready for Kubernetes with Docker, ensuring easy scaling and management.

### Technology Stack
- **Database:** [Google Cloud Platform Cloud Spanner](https://cloud.google.com/spanner?hl=en)
- **Backend:** [JDK 17](https://openjdk.org/)
- **Build Tool:** [Maven](https://maven.apache.org/)
- **Containerization:** [Docker](https://www.docker.com/)
- **Hosting:** [k8s](https://kubernetes.io/)

## Getting Started

### Prerequisites
- Maven
- JDK 17
- Docker (Preferred for local development and testing)

### Running Locally
1. **Clone the repository:**

   ```bash
   git clone git@github.com:ariessolutionsio/services-inventory.git
   cd services-inventory
   ```

2. **Build the project:**

   ```bash
   mvn clean install
   ```

3. **Run the application:**

    *Without Docker:*

   ```bash
    mvn clean install
    mvn verify
    mvn run
   ```

    *With Docker:*

   ```bash
    docker build -t services-inventory .
    docker run -p 8080:8080 services-inventory
   ```

## Utilizing the API and Contributing

### Utilizing the API
- A [Postman collection](https://github.com/ariessolutionsio/services-inventory/blob/main/Inventory_Availability.postman_collection.json) is available for testing and exploring the API endpoints.
- The OpenAPI specification provides detailed documentation on API usage and integration.

## Contributing
Contributions are welcome! Please feel free to submit pull requests or open issues to discuss proposed changes or report bugs.

For major changes, please open an issue first to discuss what you would like to change. Ensure to update tests as appropriate.

## License
TBD


----


# [MACH Booster](https://www.ariessolutions.io/composable-commerce/mach-booster-commercetools-accelerator/)

This service was generated from the template-api-service which is part of the [MACH Booster](https://www.ariessolutions.io/composable-commerce/mach-booster-commercetools-accelerator/) accelerator. The MACH Booster uses custom CLI tooling and prebuilt components to quickly build a full composable commerce solution.