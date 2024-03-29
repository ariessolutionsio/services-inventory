<p align="center">
<img alt="Aries Labs" width="300" src="docs/assets/Labs-For-Dk_BG.svg">
</p>


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

This source code is provided under the [GNU AGPLv3 license](https://www.gnu.org/licenses/agpl-3.0.en.html).

All work product released in this repository is provided ​“AS IS”. We makes no other warranties, express or implied, and hereby disclaims all implied warranties, including any warranty of merchantability and warranty of fitness for a particular purpose. The demo provided is for testing purposes only, does not include any form of SLA, and should not be used in a production environment.

If you would like to discuss alternative licensing or leveraging this application in your composable stack, please [reach out to Aries Solutions](https://www.ariessolutions.io/contact-aries/) to discuss options.


----


# [Aries Labs](https://www.ariessolutions.io/)

This project is part of the Aries Labs initiative. To learn more about Aries Solutions and other projects including the MACH Booster please visit our website. [ariessolutions.io](https://www.ariessolutions.io)