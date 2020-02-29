# Environment Variable Setup

The ~/.bash_profile ($HOME/.bash_profile) or ~/.profile file is executed when you login using console or remotely using ssh. Type the following command to edit ~/.bash_profile file, enter:

```
$ vi ~/.bash_profile
```

### 1. Server Port

Append the $SERVER_PORT settings, enter:

```
export SERVER_PORT = your_server_port
```

### 2. Database Host

Append the $DB_HOST settings, enter:

```
export DB_HOST = your_database_host
```

### 3. Database Port

Append the $DB_PORT settings, enter:

```
export DB_PORT = your_database_port
```

### 4. Database Username

Append the $DB_USER settings, enter:

```
export DB_USER = your_database_username
```

### 5. Database Password

Append the $DB_PASSWORD settings, enter:

```
export DB_PASSWORD = your_database_password
```

### 6. Database Name

Append the $DB_NAME settings, enter:

```
export DB_NAME = your_database_name
```

### 7. S3 Access Key

Append the $S3_ACCESS_KEY settings, enter:

```
export S3_ACCESS_KEY = your_s3_access_key
```

### 8. S3 Secret Key

Append the $S3_SECRET_KEY settings, enter:

```
export S3_SECRET_KEY = your_s3_secret_key
```

### 9. S3 Bucket Name

Append the $S3_BUCKET settings, enter:

```
export S3_BUCKET = your_s3_bucket_name
```

### 9. S3 Region

Append the $S3_REGION settings, enter:

```
export S3_REGION = your_s3_region
```

### 10. Username

This is used for basic authentication. Append the $USERNAME settings, enter:

```
export USERNAME = your_username
```

### 11. Secret Key

This is used for basic authentication. Append the $SECRET_KEY settings, enter:

```
export SECRET_KEY = your_secret_key
```

Save and close the file.

# NOTE

The value in each env variable is depends on your environment server.

## Branch Conventions

There are 4 kinds of branch
- features-{your-feature-name}
- enhance-{your-enhance-name}
- hotfix-{your-hotfix-name}
- bugfix-{your-bugfix-name}

## How To Create New Tag

- Checkout to master branch
- Run command to create new tag, like the following example
    ```
    git tag -a v19.07.16-01 -m "add basic trans and internal routes"
    ```
- Push your tags with command below
    ```
    git push origin --tags

## How To Deploy

- Make sure your changes/branch already in production
- Create a tag with following format
vYY.MM.DD and related message
- Inform in Deployment group