# Code-quality-web
Find out if  there are statistically significant differences in software quality of open source repositories that implement E-commerce solutions using different web frameworks.

## Project structure

``` 
└───collect data   # Scripts to collect information on repositories from GitHub.
    ├───crawling   # Scripts from experimrnt #1 using web crawling.
    ├───data   # Collected data.
    ├───get_repos_info.py   # Search repos and collect information from GitHub API.
    ├───merge_repos_info.py   # Merge data to one file.
    ├───clone_repos.py   # Clone selected repos locally.
    └───requirement.txt   # Dependences.
```

## Run 


``` 
python get_repos_info.py
```

Make sure to install dependences first e.g.

```
pip install -r requirements.txt
```




