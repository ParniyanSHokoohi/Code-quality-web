 # Xpaths of elements to work with
from selenium import webdriver
from selenium.common.exceptions import NoSuchElementException
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.chrome.options import Options  
from time import sleep 
from read_atributes.py import read_atributes
  
secs_to_wait = 5
options = Options()
# set headless option to True to run browser in headless mode
# options.headless = True 
# Start the browser
browser = webdriver.Chrome(options=options)
#github url von vue projects
github_vue_shops_url = "https://github.com/search?l=JavaScript&q=online+shop+vue&type=Repositories" 

## css selector of elements that we need.
next_page_link_selector='#js-pjax-container > div > div.col-12.col-md-9.float-left.px-2.pt-3.pt-md-0.codesearch-results > div > div.paginate-container.codesearch-pagination-container > div > a.next_page'
repo_urls_selector = 'a.v-align-middle'

# Go to the github page of online shops in Vue repos
browser.get(github_vue_shops_url)
# make a break not to be spottet as robot
sleep(secs_to_wait)

repo_urls=[]
while(True):
    # Catch github rate limit
    try:
        repo_url_elements = browser.find_elements(By.CSS_SELECTOR, repo_urls_selector)
    except NoSuchElementException as e:
        sleep(5*60)
        # Catch empty/error page
        try:
            repo_url_elements = browser.find_elements(By.CSS_SELECTOR, repo_urls_selector)
        except NoSuchElementException as e:
            break
    # fill repo_urls with links from elements
    for url_element in repo_url_elements:
        repo_urls.append(url_element.get_attribute('href'))
    # Catch last page
    try:
        next_page_link = browser.find_element(By.CSS_SELECTOR, next_page_link_selector)
        next_page_link.click()
        sleep(secs_to_wait*3)
    except NoSuchElementException as e:
        break
    sleep(secs_to_wait*3)
    
for repo_url in repo_urls:
    attributes = read_atributes(repo_url)

sleep(secs_to_wait)

browser.close()

print('Browser closed')
    