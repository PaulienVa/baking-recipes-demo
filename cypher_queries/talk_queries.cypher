CREATE DATABASE recipes;

:use recipes;

CREATE CONSTRAINT unique_recipe_name FOR (r:Recipe) REQUIRE r.name IS UNIQUE;
CREATE CONSTRAINT unique_author_name FOR (a:Author) REQUIRE a.name IS UNIQUE;
CREATE CONSTRAINT unique_ingredient_name FOR (i:Ingredient) REQUIRE i.name IS UNIQUE;


CREATE (ph:Author {name: "John Doe", website: "https://www.johndoe-recipes.com"});

CREATE (breadFlour :Ingredient {name: "Strong white bread flour"})
CREATE (plainFlour :Ingredient {name: "Plain flour"})
CREATE (salt :Ingredient {name: "Salt"})
CREATE (eggs :Ingredient {name: "Egg"})
CREATE (water :Ingredient {name: "Cold water"})
CREATE (butter :Ingredient {name: "Unsalted butter"})

CREATE (puffPastry:Recipe {
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
MATCH (puffPastry: Recipe {name: "Puff Pastry (John Doe)"})
MERGE
(ph)-[:HAS_WRITTEN {published_in: "https://www.paulhollywood.com/post/puff-pastry", publication_type: "WEBSITE"}]->(puffPastry);

MATCH (puffPastry:Recipe {name: "Puff Pastry (John Doe)"})
MATCH (breadFlour :Ingredient {name: "Strong white bread flour"})
MATCH (plainFlour :Ingredient {name: "Plain flour"})
MATCH (salt :Ingredient {name: "Salt"})
MATCH (water :Ingredient {name: "Cold water"})
MATCH (butter :Ingredient {name: "Unsalted butter"})
MERGE (puffPastry) -[:REQUIRES {quantity: 150, unit: "g"}]->(breadFlour)
MERGE (puffPastry)-[:REQUIRES {quantity: 150, unit: "g"}]->(plainFlour)
MERGE (puffPastry)-[:REQUIRES {quantity: 1, unit: "pinch"}]->(salt)
MERGE (puffPastry)-[:REQUIRES {quantity: 100, unit: "ml"}]->(water)
MERGE (puffPastry)-[:REQUIRES {quantity: 250, unit: "g"}]->(butter)
MERGE (puffPastry)-[:REQUIRES {quantity: 2}]->(eggs);

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
MATCH (puffPastry:Recipe {name: "Puff Pastry (John Doe)"})
MERGE (puffPastry)-[:IS_PREPARED_BY {order:1}]->(s1)
MERGE (puffPastry)-[:IS_PREPARED_BY {order:2}]->(s2)
MERGE (puffPastry)-[:IS_PREPARED_BY {order:3}]->(s3)
MERGE (puffPastry)-[:IS_PREPARED_BY {order:4}]->(s4)
MERGE (puffPastry)-[:IS_PREPARED_BY {order:5}]->(s5)
MERGE (puffPastry)-[:IS_PREPARED_BY {order:6}]->(s6)
MERGE (puffPastry)-[:IS_PREPARED_BY {order:7}]->(s7);


CREATE (flour:Ingredient {name: "Plain flour"});
CREATE (salt:Ingredient {name: "Salt"});
CREATE (unsaltedButter:Ingredient {name: "Unsalted butter"});
CREATE (lard:Ingredient {name: "Lard"});
CREATE (shallots:Ingredient {name: "Shallots"});
CREATE (onions:Ingredient {name: "Onions"});
CREATE (chives:Ingredient {name: "Chives"});
CREATE (sunflowerOil:Ingredient {name: "Sunflower oil"});
CREATE (eggs:Ingredient {name: "Eggs"});
CREATE (eggYolks:Ingredient {name: "Egg yolks"});
CREATE (thickCream:Ingredient {name: "Thick cream"});
CREATE (wholegrainMustard:Ingredient {name: "Wholegrain mustard"});

CREATE (tart:Recipe {
name: "Shallot, Onion & Chive Tart (John Doe)",
description: "A savoury allium tart with buttery pastry, slow-cooked shallots/onions, chives and a creamy egg filling, finished until just set and golden.",
preparationTime: duration({hours: 1}),     // includes resting + onion cooking (active estimate)
cookingTime: duration({minutes: 55}),         // 15 + 8 + 25..30 mins (from method) [[1]](https://www.paulhollywood.com/post/shallot-onion-chive-tart)
servings: 6,
difficulty: "MEDIUM",
category: "LUNCH"
})

MERGE (tart)-[:REQUIRES {quantity: 225, unit: "g"}]->(flour);
MERGE (tart)-[:REQUIRES {quantity: 1, unit: "pinch"}]->(salt);
MERGE (tart)-[:REQUIRES {quantity: 60, unit: "g"}]->(unsaltedButter);
MERGE (tart)-[:REQUIRES {quantity: 60, unit: "g"}]->(lard);
MERGE (tart)-[:REQUIRES {quantity: 8}]->(shallots);
MERGE (tart)-[:REQUIRES {quantity: 3}]->(onions);
MERGE (tart)-[:REQUIRES {quantity: 1, unit: "tbsp"}]->(chives);
MERGE (tart)-[:REQUIRES {quantity: 1, unit: "tbsp"}]->(sunflowerOil);
MERGE (tart)-[:REQUIRES {quantity: 4, unit: "medium"}]->(eggs);
MERGE (tart)-[:REQUIRES {quantity: 2, unit: "medium"}]->(eggYolks);
MERGE (tart)-[:REQUIRES {quantity: 200, unit: "ml"}]->(thickCream);
MERGE (tart)-[:REQUIRES {quantity: 1, unit: "tbsp"}]->(wholegrainMustard);

MERGE (bakingBeans:Utensil {name: "Baking beans (for blind baking)"})
MERGE (parchment:Utensil {name: "Baking parchment"})
MATCH  (tart:Recipe {name: "Shallot, Onion & Chive Tart (John Doe)"})
MERGE (tart)-[:IS_USED_IN {quantity: 1, unit: "sheet"}]->(parchment)
MERGE (tart)-[:IS_USED_IN {quantity: 1, unit: "set"}]->(bakingBeans);

MATCH (tart:Recipe {name: "Shallot, Onion & Chive Tart (John Doe)"})
CREATE (s1 :Step {instruction: "Make the pastry: combine flour and salt, then rub/blitz in the cold butter (and lard) until it resembles breadcrumbs. Add a little cold water if needed to bring it together. Rest the dough (about 30 minutes)."})
CREATE (s2 :Step {instruction: "Heat oven to 200°C / gas 6. Prepare a 23cm loose-based fluted tart tin (about 3.5cm deep). [[1]](https://www.paulhollywood.com/post/shallot-onion-chive-tart)"})
CREATE (s3 :Step {instruction: "Cook the shallots and onions slowly with a little oil/butter and a pinch of salt for at least 20 minutes, stirring occasionally, until very soft and golden. Cool. [[1]](https://www.paulhollywood.com/post/shallot-onion-chive-tart)"})
CREATE (s4 :Step {instruction: "Roll pastry to ~3mm thickness and line the tart tin, leaving excess overhanging the edge. [[1]](https://www.paulhollywood.com/post/shallot-onion-chive-tart)"})
CREATE (s5 :Step {instruction: "Blind bake: line with parchment, fill with baking beans, bake 15 minutes; remove beans/paper and bake ~8 minutes more until dry and lightly coloured. [[1]](https://www.paulhollywood.com/post/shallot-onion-chive-tart)"})
CREATE (s6 :Step {instruction: "Prepare the filling: whisk eggs (and yolks) with cream; season and mix in wholegrain mustard; stir in chopped chives."})
CREATE (s7 :Step {instruction: "Spread cooled onion mixture in the pastry case, then carefully pour in the egg mixture."})
CREATE (s8 :Step {instruction: "Bake 25–30 minutes until the filling is just set and golden. Cool slightly, then trim the overhanging pastry to finish. [[2]](https://www.sbs.com.au/food/recipe/shallot-onion-and-chive-tart/l0j1kbt6t)"})
MERGE (tart)-[:IS_PREPARED_BY {order:1}]->(s1)
MERGE (tart)-[:IS_PREPARED_BY {order:2}]->(s2)
MERGE (tart)-[:IS_PREPARED_BY {order:3}]->(s3)
MERGE (tart)-[:IS_PREPARED_BY {order:4}]->(s4)
MERGE (tart)-[:IS_PREPARED_BY {order:5}]->(s5)
MERGE (tart)-[:IS_PREPARED_BY {order:6}]->(s6)
MERGE (tart)-[:IS_PREPARED_BY {order:7}]->(s7)
MERGE (tart)-[:IS_PREPARED_BY {order:8}]->(s8);

MATCH (ph:Author {name: "John Doe"})
MATCH (tart:Recipe {name: "Shallot, Onion & Chive Tart (John Doe)"})
(ph)-[:HAS_WRITTEN {published_in: "https://www.johndoe-recipes.com/post/puff-pastry", publication_type: "WEBSITE"}]->(tart);

CREATE (madeleines :Recipe {name: "Madeleines"});
CREATE (ellen :Author {name: "Ellen Baker", website: "https://www.ellen-bakes.com"});

MATCH (madeleines :Recipe {name: "Madeleines"})
SET madeleines.description = "Little french cakes with a vanilla flavor";

MATCH (ellen :Author {name: "Ellen Baker"})
MATCH (madeleines :Recipe {name: "Madeleines"})
MERGE (ellen)-[:HAS_WRITTEN]->(madeleines);

CREATE (madeleinePan :Utensil {name: "Madeleine pan"});

MERGE (egg :Ingredient {name: "Egg"});
MERGE (granulatedSugar :Ingredient {name: "granulated sugar"});
MERGE (plainFlour :Ingredient {name: "Plain flour"});
MERGE (butter :Ingredient {name: "Unsalted butter"});
MERGE (lemonZest :Ingredient {name: "Lemon zest"});
MERGE (lemonJuice :Ingredient {name: "Lemon juice"});
MERGE (vanilla :Ingredient {name: "Vanilla extract"});
MERGE (salt :Ingredient {name: "Salt"});
MERGE (confectionersSugar :Ingredient {name: "Confectioners sugar"});

MATCH (madeleines :Recipe {name: "Madeleines"})
MERGE (madeleines)-[:REQUIRES {quantity: 2, unit: "large"}]->(egg)
MERGE (madeleines)-[:REQUIRES {quantity: 1, unit: "cup"}]->(granulatedSugar)
MERGE (madeleines)-[:REQUIRES {quantity: 1, unit: "cup"}]->(plainFlour)
MERGE (madeleines)-[:REQUIRES {quantity: 128, unit: "gram"}]->(butter)
MERGE (madeleines)-[:REQUIRES {quantity: 1, unit: "tablespoon"}]->(lemonZest)
MERGE (madeleines)-[:REQUIRES {quantity: 1, unit: "tablespoon"}]->(lemonJuice)
MERGE (madeleines)-[:REQUIRES {quantity: 1, unit: "tablespoon"}]->(vanilla)
MERGE (madeleines)-[:REQUIRES {quantity: 1, unit: "pinch"}]->(salt)
MERGE (madeleines)-[:COULD_ALSO_CONTAIN {quantity: 1, unit: "bit"}]->(confectionersSugar);