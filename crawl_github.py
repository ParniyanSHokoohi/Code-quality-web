 # Xpaths of elements to work with
from selenium import webdriver
from selenium.common.exceptions import NoSuchElementException
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.chrome.options import Options  
from time import sleep 
from read_atributes import read_atributes
import pickle
import json
  
secs_to_wait = 5
options = Options()
# set headless option to True to run browser in headless mode
# options.headless = True 
# Start the browser
# browser = webdriver.Chrome(options=options)
#github url von vue projects
github_vue_shops_url = "https://github.com/search?l=JavaScript&q=online+shop+vue&type=Repositories" 
#github url von React projects
github_react_shops_url = "https://github.com/search?l=JavaScript&q=online+shop+react&type=Repositories"
## css selector of elements that we need.
next_page_link_selector='#js-pjax-container > div > div.col-12.col-md-9.float-left.px-2.pt-3.pt-md-0.codesearch-results > div > div.paginate-container.codesearch-pagination-container > div > a.next_page'
repo_urls_selector = 'a.v-align-middle'

# Go to the github page of online shops in Vue repos
# browser.get(github_vue_shops_url)
# make a break not to be spottet as robot
sleep(secs_to_wait)

#repo_urls_react.pkl
with open('repo_urls_react.pkl','rb') as f:
    repo_urls = pickle.load(f)


# repo_urls=[]
# while(True):
#     # Catch github rate limit
#     try:
#         repo_url_elements = browser.find_elements(By.CSS_SELECTOR, repo_urls_selector)
#     except NoSuchElementException as e:
#         sleep(5*60)
#         # Catch empty/error page
#         try:
#             repo_url_elements = browser.find_elements(By.CSS_SELECTOR, repo_urls_selector)
#         except NoSuchElementException as e:
#             break
#     # fill repo_urls with links from elements
#     for url_element in repo_url_elements:
#         repo_urls.append(url_element.get_attribute('href'))
#     # Catch last page
#     try:
#         next_page_link = browser.find_element(By.CSS_SELECTOR, next_page_link_selector)
#         next_page_link.click()
#         sleep(secs_to_wait*3)
#     except NoSuchElementException as e:
#         break
#     sleep(secs_to_wait*3)

# with open('repo_urls.pkl','wb') as f:
#     pickle.dump(repo_urls, f)
print(len(repo_urls))
attrs  = {}


for i, repo_url in enumerate(repo_urls):
    # with open('attributes', 'wb') as f:
    #     pickle.dump(attrs, f)
    # with open('attributes.json', 'a') as f:
    #     json.dump(attrs, f)
    
    print(i, repo_url)
    try:
        attributes = read_atributes(repo_url)
        print(attributes)
        key_name = repo_url.split('/')[-2] + '_' + repo_url.split('/')[-1]
        print(key_name)
        attrs[key_name]=attributes
        print(attrs[key_name])
        print(str(attrs))
        with open('attributes_react.txt', 'a') as f:
            f.writelines('\n')
            f.writelines(f'{i} -- ' + repo_url)
            f.writelines('\n')
            f.writelines(str(attrs[key_name]))
            f.writelines('\n')
            f.writelines('######################################')
            print(str(attrs))
    except NoSuchElementException as e:
        sleep(5*60)
        # Catch empty/error page
        try:
            attributes = read_atributes(repo_url)
            key_name = repo_url.split('/')[-2] + '_' + repo_url.split('/')[-1]
            attrs[key_name]=attributes
            print(attributes)
            print(attrs[key_name])
            print(str(attrs))
            with open('attributes.txt', 'a') as f:
                f.writelines('\n')
                f.writelines(f'{i} -- ' + repo_url)
                f.writelines('\n')
                f.writelines(str(attrs))
                f.writelines('\n')
                f.writelines('######################################')
                #print(str(attrs))

        except NoSuchElementException as e:
            print('CONTINUE')
            continue
    
    sleep(secs_to_wait)

    print(attributes)




# browser.close()

# print('Browser closed')
    