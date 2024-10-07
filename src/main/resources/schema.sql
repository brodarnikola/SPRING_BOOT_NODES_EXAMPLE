CREATE TABLE `location_node` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(200) NOT NULL,
  `parent_node_id` INT(11) DEFAULT NULL,
  `ordering` INT(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_locationNode_parent` FOREIGN KEY (`parent_node_id`) REFERENCES `location_node` (`id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--T he foreign key (parent_node_id) is defined with ON DELETE CASCADE to automatically delete child nodes when a parent node is deleted