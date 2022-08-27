import unittest
import helpers

class TestDataCollection(unittest.TestCase):

    tests_params = {
        'count_pages': (
            {'total':7, 'per_page':50},
            1
        ),
        'get_description_length': (
            {'repo':{'description':None}},
            0
        )
    }

    def run_test_fun(self, fun):
        name = fun.__name__
        test_input, answer = self.tests_params[name]
        output = fun(**test_input)
        self.assertEqual(answer, output)

    def test_count_pages(self):
        self.run_test_fun(helpers.count_pages)
    
    def test_get_description_length(self):
        self.run_test_fun(helpers.get_description_length)

unittest.main()