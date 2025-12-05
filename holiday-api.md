# Public Holiday API - Nager.Date

# Public Holiday API

The API provides a simple way to query the holidays of over 100 countries, including long weekends.

### Resources

[API Documentation](/swagger) [GitHub Project Page](https://github.com/nager/Nager.Date) [Terms of Service](/Legal/TermsOfService)

## Request Example

Get the public holidays from the given year and country

[https://date.nager.at/api/v3/publicholidays/2025/AT](/api/v3/PublicHolidays/2025/AT)

**GET** /api/v3/PublicHolidays/{Year}/{CountryCode}

Model:

| date        | The date of the holiday|
| localName   | Local name|
| name        | English name|
| countryCode | ISO 3166-1 alpha-2|
| ~~fixed~~   | ~~Is this public holiday every year on the same date~~|
| global      | Is this public holiday in every county (federal state)|
| counties    | If it is not global you found here the Federal states (ISO-3166-2)|
| launchYear  | The launch year of the public holiday|
| types       | The types of the public holiday, several possible<br><br>- Public<br>- Bank (Bank holiday, banks and offices are closed)<br>- School (School holiday, schools are closed)<br>- Authorities (Authorities are closed)<br>- Optional (Majority of people take a day off)<br>- Observance (Optional festivity, no paid day off) |


```json
[
{
"date": "2017-01-01",
"localName": "Neujahr",
"name": "New Year's Day",
"countryCode": "AT",
"fixed": true,
"global": true,
"counties": null,
"launchYear": 1967,
"types": \[
"Public"
\]
},
{
"date": "2017-01-06",
"localName": "Heilige Drei Könige",
"name": "Epiphany",
"countryCode": "AT",
"fixed": true,
"global": true,
"counties": null,
"launchYear": null,
"types": \[
"Public"
\]
},
{
"date": "2017-04-17",
"localName": "Ostermontag",
"name": "Easter Monday",
"countryCode": "AT",
"fixed": false,
"global": true,
"counties": null,
"launchYear": 1642,
"types": \[
"Public"
\]
},
{
"date": "2017-05-01",
"localName": "Staatsfeiertag",
"name": "National Holiday",
"countryCode": "AT",
"fixed": true,
"global": true,
"counties": null,
"launchYear": 1955,
"types": \[
"Public"
\]
},
{
"date": "2017-05-25",
"localName": "Christi Himmelfahrt",
"name": "Ascension Day",
"countryCode": "AT",
"fixed": false,
"global": true,
"counties": null,
"launchYear": null,
"types": \[
"Public"
\]
},
{
"date": "2017-06-05",
"localName": "Pfingstmontag",
"name": "Whit Monday",
"countryCode": "AT",
"fixed": false,
"global": true,
"counties": null,
"launchYear": null,
"types": \[
"Public"
\]
},
{
"date": "2017-06-15",
"localName": "Fronleichnam",
"name": "Corpus Christi",
"countryCode": "AT",
"fixed": false,
"global": true,
"counties": null,
"launchYear": null,
"types": \[
"Public"
\]
},
{
"date": "2017-08-15",
"localName": "Maria Himmelfahrt",
"name": "Assumption Day",
"countryCode": "AT",
"fixed": true,
"global": true,
"counties": null,
"launchYear": null,
"types": \[
"Public"
\]
},
{
"date": "2017-10-26",
"localName": "Nationalfeiertag",
"name": "National Holiday",
"countryCode": "AT",
"fixed": true,
"global": true,
"counties": null,
"launchYear": null,
"types": \[
"Public"
\]
},
{
"date": "2017-11-01",
"localName": "Allerheiligen",
"name": "All Saints' Day",
"countryCode": "AT",
"fixed": true,
"global": true,
"counties": null,
"launchYear": null,
"types": \[
"Public"
\]
},
{
"date": "2017-12-08",
"localName": "Mariä Empfängnis",
"name": "Immaculate Conception",
"countryCode": "AT",
"fixed": true,
"global": true,
"counties": null,
"launchYear": null,
"types": \[
"Public"
\]
},
{
"date": "2017-12-25",
"localName": "Weihnachten",
"name": "Christmas Day",
"countryCode": "AT",
"fixed": true,
"global": true,
"counties": null,
"launchYear": null,
"types": \[
"Public"
\]
},
{
"date": "2017-12-26",
"localName": "Stefanitag",
"name": "St. Stephen's Day",
"countryCode": "AT",
"fixed": true,
"global": true,
"counties": null,
"launchYear": null,
"types": \[
"Public"
\]
}
\]

```