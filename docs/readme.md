# Inventory Service Docs

### Running a preview for doc development

```bash
npx @redocly/cli preview-docs https://services-inventory.api.cloud.ariessolutions.io/v3/api-docs
```

### Creating a Bundle
This will bundle the live OpenAPI specification with the config and extended content.

```bash
npx @redocly/cli bundle https://services-inventory.api.cloud.ariessolutions.io/v3/api-docs -o dist-bundle.yaml
```

### Generate the Documentation File

```bash
npx @redocly/cli build-docs dist-bundle.yaml -o index.html
```

### Linting

```bash
npx @redocly/cli lint https://services-inventory.api.cloud.ariessolutions.io/v3/api-docs
```