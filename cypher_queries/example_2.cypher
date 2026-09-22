CREATE DATABASE example2;
:use example2;

CREATE CONSTRAINT unique_recipe_name FOR (r:Recipe) REQUIRE r.name IS UNIQUE;
CREATE CONSTRAINT unique_ingredient_name FOR (i:Ingredient) REQUIRE i.name IS UNIQUE;

CREATE (r :Recipe {name: "Tuna sandwich"});
CREATE (i :Bread :Ingredient {name: "Allison Bread"});
CREATE (i :Spread :Ingredient {name: "Tuna Spread"});
CREATE (i :Vegetables :Ingredient {name: "Salad"});

MATCH (r :Recipe {name: "Tuna sandwich"})
MATCH (i1 :Ingredient {name: "Bread"})
MATCH (i2 :Ingredient {name: "Tuna Spread"})
MATCH (i3 :Ingredient {name: "Salad"})
CREATE (i1)-[:USED_IN]->(r)
CREATE (i2)-[:USED_IN]->(r)
CREATE (i3)-[:USED_IN]->(r);