# BZ Assignment

A simple Clojure web service that handles authentication and checks for sanctions.

## What this project does

This is a basic web API that:
- Lets users get authentication tokens
- Shows different types of entities
- Lists available sanction lists
- Checks if someone is on a sanction list

## How to use the API

Here are the main things you can do:

- `POST /auth/token` - Get a login token
- `GET /entity-type` - See what types of entities exist
- `GET /sanction-list` - Get the list of sanction lists
- `POST /check-sanction` - Check if someone is sanctioned

## Prerequisites to start

Before you start, make sure you have:
- Java 17 or newer
- Leiningen (the Clojure build tool)
- Git

## How to set it up

### Step 1: Get the code

```bash
git clone https://github.com/ftm01/clojure-api.git
cd clojure-api
```

### Step 2: Set up environment variables

Create a file called `.env` in the project folder and add:

```bash
JWT_SECRET=your-secret-key-here
API_KEY=your-api-key-here
APP_ID=your-app-id
APP_SECRET=your-app-secret
```

**Important**: Change these values to something secure!

### Step 3: Install dependencies

```bash
lein deps
```

### Step 4: Run the application

```bash
lein run
```

The server will start at `http://localhost:3000`

Leiningen must be installed globally in order to be able to run the project via terminal / cmd.

Navigate to the root folder of the project, for example:

```bash
cd/Desktop/bz-assignment
```

Only after navigating to the root folder, execute the lein command:

```bash
lein run
```

NOTICE: Java17 and Leiningen are e prerequisite and must be installed on the machine.

## How to test the API

Once it's running, you can try:

1. **Get a token**:
   ```bash
   curl -X POST http://localhost:3000/auth/token \
     -H "Content-Type: application/json" \
     -d '{"applicationId": "myApplicationId", "secret": "mySecret"}'
   ```

2. **Get entity types**:
   ```bash
   curl -H "Authorization: Bearer YOUR_TOKEN" \
        -H "x-api-key: my-api-key" \
        http://localhost:3000/entity-type
   ```

3. **Get sanction lists**:
   ```bash
   curl -H "Authorization: Bearer YOUR_TOKEN" \
        -H "x-api-key: my-api-key" \
        http://localhost:3000/sanction-list
   ```

4. **Check sanctions**:
   ```bash
   curl -X POST http://localhost:3000/check-sanction \
        -H "Authorization: Bearer YOUR_TOKEN" \
        -H "x-api-key: my-api-key" \
        -H "Content-Type: application/json" \
        -d '{"entity": "example-entity", "entityType": "example-type"}'
   ```

## API Documentation

You can see the full API documentation by going to:
```
http://localhost:3000/api-docs/index.html
```

This will show you all the available endpoints and how to use them.

## Method Documentation

### Core Application

#### `-main` (core.clj)
- **Purpose**: Entry point of the application
- **Function**: Starts the Jetty web server on port 3000
- **Returns**: Running web server instance

### Authentication Module (auth.clj)

#### `valid-app? [applicationId secret]`
- **Purpose**: Validates application credentials
- **Parameters**: 
  - `applicationId` (string): Application identifier
  - `secret` (string): Application secret
- **Returns**: `true` if credentials match environment variables, `false` otherwise
- **Usage**: Used during login to verify application credentials

#### `generate-token [applicationId]`
- **Purpose**: Creates a JWT token for authenticated users
- **Parameters**: 
  - `applicationId` (string): Application identifier
- **Returns**: JWT token string
- **Token Payload**: Contains user ID, admin status, name, and timestamp
- **Usage**: Called after successful authentication

#### `verify-token [token]`
- **Purpose**: Validates a JWT token
- **Parameters**: 
  - `token` (string): JWT token to verify
- **Returns**: Decoded token payload if valid, `nil` if invalid
- **Usage**: Used in middleware to authenticate API requests

### Middleware (middleware.clj)

#### `unauthorized [msg]`
- **Purpose**: Creates a standardized unauthorized response
- **Parameters**: 
  - `msg` (string): Error message
- **Returns**: HTTP response map with 401 status and JSON error body
- **Usage**: Called when authentication fails

#### `wrap-authentication [handler]`
- **Purpose**: Middleware that validates API key and JWT token
- **Parameters**: 
  - `handler` (function): Next handler in the chain
- **Returns**: Wrapped handler function
- **Checks**: 
  - Validates `x-api-key` header
  - Validates `Authorization: Bearer <token>` header
  - Verifies JWT token signature
- **Usage**: Applied to protected API routes

### Controllers

#### Authentication Controller (authcontroller.clj)

##### `token-handler [{:keys [parameters]}]`
- **Purpose**: Handles login requests and returns JWT tokens
- **Parameters**: 
  - `parameters` (map): Request parameters containing `applicationId` and `secret`
- **Returns**: 
  - `200`: Success response with JWT token
  - `401`: Error response for invalid credentials
- **Usage**: POST `/auth/token` endpoint

#### Entity Controller (entitycontroller.clj)

##### `entity-type-handler [_]`
- **Purpose**: Returns available entity types
- **Parameters**: Request map (unused)
- **Returns**: `200` response with list of entity types
- **Data**: Returns predefined entity types (Company, Individual)
- **Usage**: GET `/api/entity-type` endpoint

#### Sanction Controller (sanctioncontroller.clj)

##### `sanction-list-handler [_]`
- **Purpose**: Returns available sanction lists
- **Parameters**: Request map (unused)
- **Returns**: `200` response with list of sanction lists
- **Data**: Returns predefined sanction lists (EU Sanctions, Israeli MOD)
- **Usage**: GET `/api/sanction-list` endpoint

##### `check-sanction-handler [{:keys [body-params]}]`
- **Purpose**: Validates and processes sanction check requests
- **Parameters**: 
  - `body-params` (map): Request body containing entity information
- **Returns**: 
  - `200`: Success response with sanction check results
  - `400`: Error response with validation details
- **Process**: 
  1. Validates request body using validation functions
  2. Returns mock sanction check results if validation passes
- **Usage**: POST `/api/check-sanction` endpoint

### Validation (check_sanction.clj)

#### `string-number? [s]`
- **Purpose**: Checks if a string represents a number
- **Parameters**: 
  - `s` (string): String to validate
- **Returns**: `true` if string contains only digits, `false` otherwise
- **Usage**: Helper function for threshold validation

#### `validate-check-request [body]`
- **Purpose**: Validates sanction check request parameters
- **Parameters**: 
  - `body` (map): Request body to validate
- **Returns**: Map of validation errors or `nil` if valid
- **Validations**:
  - `full_name`: Required string
  - `threshold`: Required string representing a number
  - `gender`: Optional, must be "Male" or "Female"
  - `birth_date`: Optional string
  - `country_code`: Optional string
  - `address`: Optional string
  - `entity_types`: Required array of entity type objects
  - `sanction_lists`: Required array of sanction list objects
  - `free_text`: Required string
- **Usage**: Called by `check-sanction-handler` to validate requests

### Data Schemas

#### Authentication Schema (auth_token.clj)
- **`auth-request`**: Schema for login requests (applicationId, secret)
- **`token-response`**: Schema for token responses (token)

#### Entity Type Schema (entity_type.clj)
- **`entity-type`**: Schema for entity type objects (id, type)
- **`entity-type-response`**: Schema for entity type list responses

#### Sanction Schema (sanction.clj)
- **`sanction-list`**: Schema for sanction list responses
- **`check-sanction-request`**: Schema for sanction check requests
- **`check-sanction-response`**: Schema for sanction check responses

#### Error Schema (error.clj)
- **`error-response`**: Schema for error responses (error message)

### Route Configuration (routes.clj)

The application uses Reitit for routing with the following structure:

- **`/swagger.json`**: Swagger API documentation
- **`/auth/token`**: Authentication endpoint
- **`/api/*`**: Protected API endpoints (requires authentication)
  - `/api/entity-type`: Get entity types
  - `/api/sanction-list`: Get sanction lists
  - `/api/check-sanction`: Check sanctions
- **`/api-docs/*`**: Swagger UI documentation

All protected routes use the `wrap-authentication` middleware for security.

## Running tests

To run all the tests:

```bash
lein test
```

To see how much of your code is tested:

```bash
lein cloverage
```

## Project structure

The code is organized like this:

```
bz-assignment/
├── src/bz_assignment/
│   ├── auth.clj                 # Handles login
│   ├── controller/              # API endpoints
│   ├── middleware.clj           # Request processing
│   ├── routes.clj              # URL routing
│   ├── schema/                 # Data structures
│   └── validation/             # Input checking
├── spec/                       # Specifications
├── test/                       # Test files
├── resources/                  # Static files
└── doc/                        # Documentation
```

## Deploying to AWS

### ECS

This is the easiest way deploy on AWS.

#### Prerequisites:
- AWS account
- AWS CLI installed
- Docker installed

#### Steps:

1. **Build and upload the app**:
   ```bash
   docker build -t bz-assignment .
   docker tag bz-assignment:latest YOUR_ACCOUNT.dkr.ecr.REGION.amazonaws.com/bz-assignment:latest
   aws ecr get-login-password --region REGION | docker login --username AWS --password-stdin YOUR_ACCOUNT.dkr.ecr.REGION.amazonaws.com
   docker push YOUR_ACCOUNT.dkr.ecr.REGION.amazonaws.com/bz-assignment:latest
   ```

2. **Create an ECS cluster**:
   ```bash
   aws ecs create-cluster --cluster-name bz-assignment-cluster
   ```

3. **Create a task definition file** (save as `task-definition.json`):
   ```json
   {
     "family": "bz-assignment",
     "networkMode": "awsvpc",
     "requiresCompatibilities": ["FARGATE"],
     "cpu": "256",
     "memory": "512",
     "executionRoleArn": "arn:aws:iam::YOUR_ACCOUNT:role/ecsTaskExecutionRole",
     "containerDefinitions": [
       {
         "name": "bz-assignment",
         "image": "YOUR_ACCOUNT.dkr.ecr.REGION.amazonaws.com/bz-assignment:latest",
         "portMappings": [
           {
             "containerPort": 3000,
             "protocol": "tcp"
           }
         ],
         "environment": [
           {
             "name": "JWT_SECRET",
             "value": "your-production-secret"
           },
           {
             "name": "API_KEY",
             "value": "your-production-api-key"
           },
           {
             "name": "APP_ID",
             "value": "your-production-app-id"
           },
           {
             "name": "APP_SECRET",
             "value": "your-production-app-secret"
           }
         ]
       }
     ]
   }
   ```

4. **Register the task**:
   ```bash
   aws ecs register-task-definition --cli-input-json file://task-definition.json
   ```

5. **Create a service**:
   ```bash
   aws ecs create-service \
     --cluster bz-assignment-cluster \
     --service-name bz-assignment-service \
     --task-definition bz-assignment:1 \
     --desired-count 1 \
     --launch-type FARGATE \
     --network-configuration "awsvpcConfiguration={subnets=[subnet-12345678],securityGroups=[sg-12345678],assignPublicIp=ENABLED}"
   ```

## Security

Make sure to:
- Use strong passwords for the environment variables
- Set up proper security groups in AWS
- Keep your AWS credentials secure
- Regularly update your dependencies

## Monitoring

To see what's happening with the app:
- Check CloudWatch logs
- Set up alarms for high CPU usage
- Monitor your application logs

## Common problems

1. **App won't start**: Check your environment variables
2. **Can't connect**: Make sure your security groups allow traffic on port 3000
3. **Tests failing**: Make sure all dependencies are installed

## Getting help

If you have problems:
1. Check the logs
2. Make sure all dependencies are installed
3. Verify your environment variables are set correctly
4. Check that your AWS permissions are correct

