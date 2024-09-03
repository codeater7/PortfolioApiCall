


# Overview
The FA Platform is designed to manage various types of investment portfolios. Each portfolio may contain numerous transactions, which can include activities such as buys, sells, dividends, and coupon payments.

The objective of this project is to develop a back-end service that generates CSV reports by fetching data from the FA Platform.

Security is taken into account; and implementation of Basic authentication is done for the API that project service exposes.

CSV file includes:
- Portfolio's shortName
- Security name
- Security ISIN code
- Transaction's currency code
- Amount ( number of 'units' in the transaction)
- Unit price
- Trade amount
- Type name
- Transaction date
- Settlement date

### Running  the Project

   #### Prerequisites ( if running the project locally )
   - [ ] Ask for the credentials that are being used by the project.

   Credentials that needs to be injected into the project are:

   ### FA ( API provider) related credentials

   ```
   fa.username=${FA-USERNAME}
   fa.password=${FA-PASSWORD}

   ```

   #### Application related credentials
  ```
   credentials.username=${USERNAME}
   credentials.password=${PASSWORD}
   credentials.username1=${USERNAME1}
   credentials.password1=${PASSWORD1}

```

- [ ] Once the project is cloned; correct credentials are provided and built, the project should start.
- [ ] The application by default runs on Port 8080 but this can be changed.


---------------------------------------------------------------------------------------------------------------


#### API Endpoints:

- The Endpoint supports **startDate** and **endDate** as query Params. Please provide the year in the form **[YYYY-MM-DD]** format.

- As of this writing, only  portfolio with *ID 3, 216 and 298* are accessible.

- Please when making the request, please include the Auth type as *Basic Auth* with the set/provided {USERNAME} and {PASSWORD} but not the creadentials from FA.

###### Simple Request to write to transaction data in CSV file looks like this:

```
http://localhost:8080/api/portfolio/216?startDate=2020-01-01&endDate=2020-11-0

```
Once the request is successfull, you should get the reponse stating that 'Successfully written data to file' with status code of 200.



###### Reading the content without saving to the file.

```
http://localhost:8080/api/portfolio/216/readonly?startDate=2020-01-01&endDate=2020-11-0

```
Once the request is successfull, you should get the reponse with the  transactions and with the status code of 200. The data will not be written to the server though.


###### Downloading the csv file endpoint.

```
http://localhost:8080/api/portfolio/216/download?startDate=2020-01-01&endDate=2020-11-0

```

## Further Reading

### 	[Documentation regarding Integration with FA](https://documentation.fasolutions.com/en/api-integrations.html)

### 	[GraphQL custom Scalar ](https://www.danvega.dev/blog/graphql-custom-scalars)

###  [Basic Auth in Spring ](https://spring.io/guides/gs/securing-web)


