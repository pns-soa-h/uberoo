INSERT INTO TAG (id, label) VALUES
  (1, 'asian'),
  (2, 'french');

INSERT INTO RESTAURANT (id, name) VALUES
  (5, 'LE Resto'),
  (6, 'Asian Resto');

INSERT INTO MEAL (id, label, price, description, restaurant_id, tag_id, category) VALUES
  (3, 'Jambon-Beurre', 4.99, 'Un sandwich, avec du jambon, et du beurre.', 5, 2, 'plat'),
  (4, 'Soupe miso', 4.99, 'Une bonne soupe', 6, 1, 'entree'),
  (5, 'Ramen', 8.99, 'Un bon ramen', 6, 1, 'plat'),
  (6, 'Glace coco', 3.99, 'Une bonne glace coco', 6, 1, 'dessert');
