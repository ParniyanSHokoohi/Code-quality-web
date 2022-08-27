# Code-quality-web
Find out if  there are statistically significant differences in software quality of open source repositories that implement E-commerce solutions using different web frameworks.

## Project structure

``` 
└───collect data   # Scripts to collect information on repositories from GitHub.
    ├───crawling   # Scripts from experimrnt #1 using web crawling.
    ├───data   # Collected data.
    ├───main.py   # Search repos and collect information from GitHub API.
    ├───ReposReader.py   # Class ReposReader that implements the functionality.
    ├───test.py   # Unit tests.
    ├───requirement.txt   # Dependences.
```

## Run 


``` 
python main.py
```

Make sure to install dependences first e.g.

```
pip install -r requirements.txt
```

## Run tests

``` 
python test.py
``` 