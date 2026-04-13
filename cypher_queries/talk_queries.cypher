// ------ Example 1 ------

CREATE DATABASE example1;
:use example1;

CREATE CONSTRAINT unique_recipe_name FOR (r:Recipe) REQUIRE r.name IS UNIQUE;
CREATE CONSTRAINT unique_ingredient_name FOR (i:Ingredient) REQUIRE i.name IS UNIQUE;

CREATE (r :Recipe {name: "Tuna sandwich"});
CREATE (i :Ingredient {name: "Bread"});
CREATE (i :Ingredient {name: "Tuna Spread"});
CREATE (i :Ingredient {name: "Salad"});

MATCH (r :Recipe {name: "Tuna sandwich"})
MATCH (i1 :Ingredient {name: "Bread"})
MATCH (i2 :Ingredient {name: "Tuna Spread"})
MATCH (i3 :Ingredient {name: "Salad"})
CREATE (i1)-[:USED_IN]->(r)
CREATE (i2)-[:USED_IN]->(r)
CREATE (i3)-[:USED_IN]->(r);

// ------ Example 2 ------

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

// ------ Recipe Application ------
CREATE DATABASE recipes;

:use recipes;

// CONSTRAINTS:

CREATE CONSTRAINT unique_recipe_name FOR (r:Recipe) REQUIRE r.name IS UNIQUE;
CREATE CONSTRAINT unique_author_name FOR (a:Author) REQUIRE a.name IS UNIQUE;
CREATE CONSTRAINT unique_ingredient_name FOR (i:Ingredient) REQUIRE i.name IS UNIQUE;




// John Doe - Puff Pastry
// Sources: method concepts from [[8]](https://www.paulhollywood.com/post/puff-pastry)
// ingredient quantities aligned with [[2]](https://www.sbs.com.au/food/recipe/paul-hollywoods-puff-pastry/o4fdsro1w)
// Review all the creation queries

CREATE (ph:Author {name: "John Doe", website: "https://www.johndoe-recipes.com"});

CREATE (breadFlour :Ingredient {name: "Strong white bread flour"});
CREATE (plainFlour :Ingredient {name: "Plain flour"});
CREATE (salt :Ingredient {name: "Salt"});
CREATE (eggs :Ingredient {name: "Egg"});
CREATE (water :Ingredient {name: "Cold water"});
CREATE (butter :Ingredient {name: "Unsalted butter"});

CREATE (r:Recipe {
name:            "Puff Pastry (John Doe)",
description:     "Classic laminated puff pastry: keep dough and butter cold; roll, fold and chill to build layers.",
preparationTime: duration({hours: 2}),
waitingTime:     duration({hours: 24}),
cookingTime:     duration({hours: 0}),
servings:        1, // “1 batch”
difficulty:      "HARD",
category:        "BAKING"
});

MATCH (ph:Author {name: "John Doe"})
MATCH (r: Recipe {name: "Puff Pastry (John Doe)"})
CREATE
(ph)-[:HAS_WRITTEN {published_in: "https://www.paulhollywood.com/post/puff-pastry", publication_type: "WEBSITE"}]->(r);

MATCH (r:Recipe {name: "Puff Pastry (John Doe)"})
MATCH (breadFlour :Ingredient {name: "Strong white bread flour"})
MATCH (plainFlour :Ingredient {name: "Plain flour"})
MATCH (salt :Ingredient {name: "Salt"})
MATCH (water :Ingredient {name: "Cold water"})
MATCH (butter :Ingredient {name: "Unsalted butter"})
CREATE (r) -[:REQUIRES {quantity: 150, unit: "g"}]->(breadFlour)
CREATE (r)-[:REQUIRES {quantity: 150, unit: "g"}]->(plainFlour)
CREATE (r)-[:REQUIRES {quantity: 1, unit: "pinch"}]->(salt)
CREATE (r)-[:REQUIRES {quantity: 100, unit: "ml"}]->(water)
CREATE (r)-[:REQUIRES {quantity: 250, unit: "g"}]->(butter);

CREATE (r)-[:REQUIRES {quantity: 2}]->(eggs);

// Steps based on John Doe page guidance: keep flour/dough/butter cold,
// chill between folds, butter covers bottom two-thirds, roll and fold repeatedly. [[8]](https://www.paulhollywood.com/post/puff-pastry)
MERGE(s1:Step {
instruction:    "Put the flours, salt, eggs and water into a large bowl and gently mix to an even dough with your fingers. Transfer the dough to a lightly floured surface and knead it for 5-10 minutes until smooth. The dough should feel a little tight at this stage. Shape the dough into a ball and put it into a plastic bag in the fridge to chill overnight, or for at least 7 hours",
estimated_time: duration({minutes: 20}), waiting_time: duration({hours: 7}) })
MERGE
(s2:Step {instruction:"Flatten the butter into a rectangle, about 40 x 19cm, by battering it down with your rolling pin. (You may find this easier to do this if you sandwich the butter between 2 sheets of cling film). Return to the fridge for an hour to harden the butter again.", estimated_time: duration({minutes:15}), waiting_time: duration({hours:1}) })
MERGE (s3:Step {instruction:"Roll out your dough to a rectangle, about 60 x 20cm wide. Put the butter on the dough so it covers the bottom two-thirds. Make sure that it is positioned neatly and covers almost to the edges."})
MERGE (s4:Step {instruction:"Lift the exposed dough at the top and fold it down over half of the butter, then fold the butter-covered bottom half of the dough up over the top. You will now have a sandwich of two layers of butter and three of dough . Pinch the edges together to seal. Put it back in a plastic bag and chill for 1 hour.", waiting_time: duration({hours:1})})
MERGE (s5:Step {instruction:"Take the dough out of the bag and put it on a lightly floured surface with a short end towards you. Roll out to a rectangle as before, keeping the edges as even as possible. Fold the top quarter down and the bottom quarter up so they meet neatly in the centre. Then fold the dough in half along the centre line. This is called a book turn. Chill in the bag for 1 hour.", waiting_time: duration({hours:1})})
MERGE (s6:Step {instruction:"Take the dough out of the bag, put it on a lightly floured surface with the short end towards you and roll into a rectangle as before. This time, fold down one-third of the dough and then fold up the bottom third to make a neat square. This is called a single turn. Chill in the bag for another hour.", waiting_time: duration({hours:1})})
MERGE (s7:Step {instruction:"Bring your dough out again and do a single turn as previously. Chill in the bag overnight. Your dough is now ready to use.", waiting_time: duration({hours:12})})
MATCH (r:Recipe {name: "Puff Pastry (John Doe)"})
CREATE (r)-[:IS_PREPARED_BY {order:1}]->(s1)
CREATE (r)-[:IS_PREPARED_BY {order:2}]->(s2)
CREATE (r)-[:IS_PREPARED_BY {order:3}]->(s3)
CREATE (r)-[:IS_PREPARED_BY {order:4}]->(s4)
CREATE (r)-[:IS_PREPARED_BY {order:5}]->(s5)
CREATE (r)-[:IS_PREPARED_BY {order:6}]->(s6)
CREATE (r)-[:IS_PREPARED_BY {order:7}]->(s7);


///


// Shallot, Onion & Chive Tart (John Doe)
// Method timings/temps from [[1]](https://www.paulhollywood.com/post/shallot-onion-chive-tart)
// Recipe structure corroborated by [[2]](https://www.sbs.com.au/food/recipe/shallot-onion-and-chive-tart/l0j1kbt6t)

// --- Ingredients (MERGE to avoid duplicates across recipes) ---
MERGE (flour:Ingredient {name: "Plain flour"})
MERGE (salt:Ingredient {name: "Salt"})
MERGE (unsaltedButter:Ingredient {name: "Unsalted butter"})
MERGE (lard:Ingredient {name: "Lard"})
MERGE (shallots:Ingredient {name: "Shallots"})
MERGE (onions:Ingredient {name: "Onions"})
MERGE (chives:Ingredient {name: "Chives"})
MERGE (sunflowerOil:Ingredient {name: "Sunflower oil"})
MERGE (eggs:Ingredient {name: "Eggs"})
MERGE (eggYolks:Ingredient {name: "Egg yolks"})
MERGE (thickCream:Ingredient {name: "Thick cream"})
MERGE (wholegrainMustard:Ingredient {name: "Wholegrain mustard"})

// --- Recipe node ---
CREATE (r:Recipe {
name: "Shallot, Onion & Chive Tart (John Doe)",
description: "A savoury allium tart with buttery pastry, slow-cooked shallots/onions, chives and a creamy egg filling, finished until just set and golden.",
preparationTime: duration({hours: 1}),     // includes resting + onion cooking (active estimate)
cookingTime: duration({minutes: 55}),         // 15 + 8 + 25..30 mins (from method) [[1]](https://www.paulhollywood.com/post/shallot-onion-chive-tart)
servings: 6,
difficulty: "MEDIUM",
category: "LUNCH"
})

// --- Required ingredients (quantities: fill TODO values from the recipe page) ---
// Pastry: flour + salt + butter (+ lard per SBS structure) [[2]](https://www.sbs.com.au/food/recipe/shallot-onion-and-chive-tart/l0j1kbt6t)
CREATE (r)-[:REQUIRES {quantity: 225, unit: "g"}]->(flour)
CREATE (r)-[:REQUIRES {quantity: 1, unit: "pinch"}]->(salt)

// 60g butter is visible on the John Doe page snippet [[1]](https://www.paulhollywood.com/post/shallot-onion-chive-tart)
CREATE (r)-[:REQUIRES {quantity: 60, unit: "g"}]->(unsaltedButter)

CREATE (r)-[:REQUIRES {quantity: 60, unit: "g"}]->(lard)

// Filling: slow-cooked shallots/onions; chives; egg+cream+mustard mixture (seen in method snippet) [[1]](https://www.paulhollywood.com/post/shallot-onion-chive-tart)
CREATE (r)-[:REQUIRES {quantity: 8}]->(shallots)
CREATE (r)-[:REQUIRES {quantity: 3}]->(onions)
CREATE (r)-[:REQUIRES {quantity: 1, unit: "tbsp"}]->(chives)
CREATE (r)-[:REQUIRES {quantity: 1, unit: "tbsp"}]->(sunflowerOil)
CREATE (r)-[:REQUIRES {quantity: 4, unit: "medium"}]->(eggs)
CREATE (r)-[:REQUIRES {quantity: 2, unit: "medium"}]->(eggYolks)
CREATE (r)-[:REQUIRES {quantity: 200, unit: "ml"}]->(thickCream)
CREATE (r)-[:REQUIRES {quantity: 1, unit: "tbsp"}]->(wholegrainMustard);

// Optional “pantry/equipment-adjacent” items, if you still want them as optional ingredients
MERGE (bakingBeans:Utensil {name: "Baking beans (for blind baking)"})
MERGE (parchment:Utensil {name: "Baking parchment"})
MATCH  (r:Recipe {name: "Shallot, Onion & Chive Tart (John Doe)"})
CREATE (r)-[:IS_USED_IN {quantity: 1, unit: "sheet"}]->(parchment)
CREATE (r)-[:IS_USED_IN {quantity: 1, unit: "set"}]->(bakingBeans);

// --- Steps (based on visible method text/timings) ---
MATCH (r:Recipe {name: "Shallot, Onion & Chive Tart (John Doe)"})
CREATE (s1:Step {instruction: "Make the pastry: combine flour and salt, then rub/blitz in the cold butter (and lard) until it resembles breadcrumbs. Add a little cold water if needed to bring it together. Rest the dough (about 30 minutes)."})
CREATE (s2:Step {instruction: "Heat oven to 200°C / gas 6. Prepare a 23cm loose-based fluted tart tin (about 3.5cm deep). [[1]](https://www.paulhollywood.com/post/shallot-onion-chive-tart)"})
CREATE (s3:Step {instruction: "Cook the shallots and onions slowly with a little oil/butter and a pinch of salt for at least 20 minutes, stirring occasionally, until very soft and golden. Cool. [[1]](https://www.paulhollywood.com/post/shallot-onion-chive-tart)"})
CREATE (s4:Step {instruction: "Roll pastry to ~3mm thickness and line the tart tin, leaving excess overhanging the edge. [[1]](https://www.paulhollywood.com/post/shallot-onion-chive-tart)"})
CREATE (s5:Step {instruction: "Blind bake: line with parchment, fill with baking beans, bake 15 minutes; remove beans/paper and bake ~8 minutes more until dry and lightly coloured. [[1]](https://www.paulhollywood.com/post/shallot-onion-chive-tart)"})
CREATE (s6:Step {instruction: "Prepare the filling: whisk eggs (and yolks) with cream; season and mix in wholegrain mustard; stir in chopped chives."})
CREATE (s7:Step {instruction: "Spread cooled onion mixture in the pastry case, then carefully pour in the egg mixture."})
CREATE (s8:Step {instruction: "Bake 25–30 minutes until the filling is just set and golden. Cool slightly, then trim the overhanging pastry to finish. [[2]](https://www.sbs.com.au/food/recipe/shallot-onion-and-chive-tart/l0j1kbt6t)"})
CREATE (r)-[:IS_PREPARED_BY {order:1}]->(s1)
CREATE (r)-[:IS_PREPARED_BY {order:2}]->(s2)
CREATE (r)-[:IS_PREPARED_BY {order:3}]->(s3)
CREATE (r)-[:IS_PREPARED_BY {order:4}]->(s4)
CREATE (r)-[:IS_PREPARED_BY {order:5}]->(s5)
CREATE (r)-[:IS_PREPARED_BY {order:6}]->(s6)
CREATE (r)-[:IS_PREPARED_BY {order:7}]->(s7)
CREATE (r)-[:IS_PREPARED_BY {order:8}]->(s8);

MATCH (ph:Author {name: "John Doe"})
MATCH (r:Recipe {name: "Shallot, Onion & Chive Tart (John Doe)"})
CREATE
(ph)-[:HAS_WRITTEN {published_in: "https://www.johndoe-recipes.com/post/puff-pastry", publication_type: "WEBSITE"}]->(r);


/// Madeleines
CREATE (n :Recipe {name: "Madeleines"});
CREATE (a :Author {name: "Ellen Baker", website: "https://www.ellen-bakes.com"});
MATCH (n :Recipe {name: "Madeleines"})
SET n.description = "Little french cakes with a vanilla flavor";

MATCH (a :Author {name: "Ellen Baker"})
MATCH (n :Recipe {name: "Madeleines"})
MERGE (a)-[:HAS_WRITTEN]->(n);

CREATE (u :Utensil {name: "Madeleine pan"});

MATCH (egg :Ingredient {name: "Egg"})
MATCH (r :Recipe {name: "Madeleines"})
MERGE (r)-[:REQUIRES {quantity: 2, unit: "large"}]->(egg);

CREATE (sugar :Ingredient {name: "granulated sugar"});

MATCH (sugar :Ingredient {name: "granulated sugar"})
MATCH (r :Recipe {name: "Madeleines"})
MERGE (r)-[:REQUIRES {quantity: 1, unit: "cup"}]->(sugar);

MATCH (plainFlour :Ingredient {name: "Plain flour"})
MATCH (r :Recipe {name: "Madeleines"})
MERGE (r)-[:REQUIRES {quantity: 1, unit: "cup"}]->(plainFlour);

MATCH (butter :Ingredient {name: "Unsalted butter"})
MATCH (r :Recipe {name: "Madeleines"})
MERGE (r)-[:REQUIRES {quantity: 128, unit: "gram"}]->(plainFlour);

CREATE (lemon :Ingredient {name: "Lemon zest"});
MATCH (lemon :Ingredient {name: "Lemon zest"})
MATCH (r :Recipe {name: "Madeleines"})
MERGE (r)-[:REQUIRES {quantity: 1, unit: "tablespoon"}]->(lemon);

CREATE (lemon :Ingredient {name: "Lemon juice"});
MATCH (lemon :Ingredient {name: "Lemon juice"})
MATCH (r :Recipe {name: "Madeleines"})
MERGE (r)-[:REQUIRES {quantity: 1, unit: "tablespoon"}]->(lemon);

CREATE (vannila :Ingredient {name: "Vanilla extract"});
MATCH (vannila :Ingredient {name: "Vanilla extract"})
MATCH (r :Recipe {name: "Madeleines"})
MERGE (r)-[:REQUIRES {quantity: 1, unit: "tablespoon"}]->(lemon);

MATCH (r :Recipe {name: "Madeleines"})
CREATE (r)-[:REQUIRES {quantity: 1, unit: "pinch"}]->(salt);

CREATE (sugar :Ingredient {name: "Confectioners sugar"});

MATCH (sugar :Ingredient {name: "Confectioners sugar"})
MATCH (r :Recipe {name: "Madeleines"})
MERGE (r)-[:COULD_ALSO_CONTAIN {quantity: 1, unit: "bit"}]->(sugar);