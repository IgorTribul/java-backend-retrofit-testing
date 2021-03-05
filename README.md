# java-backend-retrofit-testing  CHECK-LIST

                                    Expected	  Actual	Test result
1. DeleteProductTests				
1.1.	deleteProductPositiveTest	    200	        200	    Passed
1.2.	deleteProductRepeatTest	      200 (204)	  500	    Failed
1.3.	deleteProductByNotFoundIdTest	404	        500	    Failed
1.4.	deleteProductWithoutIdTest	  400 (404) 	500	    Failed
1.5.	deleteProductWitStringIdTest	400	        400	    Passed
2. GetCategoryTests				
2.1.	getCategoryByIdTest()	        200	        200	    Passed
2.2.	getAllCategoriesTest	        404	        404	    Passed
2.3.	getCategoryByNotFoundIdTest	  404	        404	    Passed
2.4.	getCategoryByStringIdTest	    400	        400	    Passed
3. GetProductTests 				
3.1.	getAllProductsTest	          200	        200	    Passed
3.2.	getProductByIdTest	          200	        200	    Passed
3.3.	getProductByNotFoundIdTest	  404	        404	    Passed
3.4.	getProductByStringIdTest	    400	        400	    Passed
4. PostProductTests				
4.1.	createProductPositiveTest	    201	        201	    Passed
4.2.	createProductWithId
                     NegativeTest	  400	        400	    Passed
4.3.	createProductWith
          FakeCategoryTitleTest	    400 (404)	  500	    Failed
4.4.	createProductWith
                      EmptyBodyTest 400 (404)	  500	    Failed
5. PutProductTests				
5.1.	updateProductPositiveTest	    201	        201	    Passed
5.2.	updateProductWith
                    BadCategoryTest	400 (404)	  500	    Failed
5.3.	updateProductNegativeTest	    400	        400	    Passed

