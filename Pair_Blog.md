# Assignment II Pair Blog Template

## Task 1) Code Analysis and Refactoring ⛏️

### a) From DRY to Design Patterns

[Links to your merge requests](https://nw-syd-gitlab.cseunsw.tech/COMP2511/24T2/teams/W15C_CABBAGE/assignment-ii/-/merge_requests/3)

> i. Look inside src/main/java/dungeonmania/entities/enemies. Where can you notice an instance of repeated code? Note down the particular offending lines/methods/fields.


An instance of repeated code can be seen in the move method in ZombieToast and Mercenary.

There are two areas of repetition in the move methods in ZombieToast and Mercenary.
1. The check for invincibility potion, in lines 102 to 129 in Mercenary.java and lines 26 to 53 in ZombieToast.java

```
if (map.getPlayer().getEffectivePotion() instanceof InvincibilityPotion) {
        Position plrDiff = Position.calculatePositionBetween(map.getPlayer().getPosition(), getPosition());

        Position moveX = (plrDiff.getX() >= 0) ? Position.translateBy(getPosition(), Direction.RIGHT)
                : Position.translateBy(getPosition(), Direction.LEFT);
        Position moveY = (plrDiff.getY() >= 0) ? Position.translateBy(getPosition(), Direction.UP)
                : Position.translateBy(getPosition(), Direction.DOWN);
        Position offset = getPosition();
        if (plrDiff.getY() == 0 && map.canMoveTo(this, moveX))
            offset = moveX;
        else if (plrDiff.getX() == 0 && map.canMoveTo(this, moveY))
            offset = moveY;
        else if (Math.abs(plrDiff.getX()) >= Math.abs(plrDiff.getY())) {
            if (map.canMoveTo(this, moveX))
                offset = moveX;
            else if (map.canMoveTo(this, moveY))
                offset = moveY;
            else
                offset = getPosition();
        } else {
            if (map.canMoveTo(this, moveY))
                offset = moveY;
            else if (map.canMoveTo(this, moveX))
                offset = moveX;
            else
                offset = getPosition();
        }
        nextPos = offset;
    }
```

2. The random movement: lines 93 to 101 in Mercenary.java and lines 55 to 61 in ZombieToast.java.

```
List<Position> pos = getPosition().getCardinallyAdjacentPositions();
pos = pos.stream().filter(p -> map.canMoveTo(this, p)).collect(Collectors.toList());
if (pos.size() == 0) {
    nextPos = getPosition();
    map.moveTo(this, nextPos);
} else {
    nextPos = pos.get(randGen.nextInt(pos.size()));
    map.moveTo(this, nextPos);
}
```


> ii. What Design Pattern could be used to improve the quality of the code and avoid repetition? Justify your choice by relating the scenario to the key characteristics of your chosen Design Pattern.
The Strategy Design Pattern would be also able to improve the quality of the code and avoid repetition in the repetition in the move method. "Strategy is a behavioral design pattern that lets you define a family of algorithms, put each of them into a separate class, and make their objects interchangeable." - RefactoringGuru.

We define a family of algorithms i.e. a family of ways to move, and because each enemy has a different interchangeable way of moving, this pattern would be perfect for it. In our code, we can then put the same movement strategies into their own classes, which would eliminate the need to repeat the code, and instead just change the movement strategy of the class.

> iii. Using your chosen Design Pattern, refactor the code to remove the repetition.
For the two cases of repetition in the move method of ZombieToast and Mercenary, we create an interface MovingStrategy which is implemented by all diferent types of other strategies such as Movingmercenarystrategy, movingfrominvinciblestategy and movingRandomStrategy. Each class that implements these strategies willt hen ahev a MovingStrategy attribute called movingStrategy. When a new moving strategy is needed, we change the movingStrategy variable, and call its method move().

### b) Observer Pattern
https://nw-syd-gitlab.cseunsw.tech/COMP2511/24T2/teams/W15C_CABBAGE/assignment-ii/-/merge_requests/5

> Identify one place where the Observer Pattern is present in the codebase, and outline how the implementation relates to the key characteristics of the Observer Pattern.  

Observer is a behavioral design pattern that lets you define a subscription mechanism to notify multiple objects about any events that happen to the object they’re observing. - Refactoring Guru.

The Observer pattern is present in the codebase with the interaction between the Switch and Bomb classes. Here’s how the implementation relates to the key characteristics of the Observer Pattern:

##### Switch as the Observable
Subscription Mechanism: The Switch class implements methods to manage its observers.

Add Observer: The subscribe() method allows Bomb instances to register themselves as observers.

Remove Observer: The unsubscribe() method allows Bomb instances to deregister themselves, stopping further notifications.

List of Observers: The Switch class maintains a list of its subscribers (List<Bomb> bombs) which holds references to all the Bomb instances that are observing the Switch.

Notify Observers: The subscribe(Bomb bomb, GameMap map) method notifies all registered Bomb instances by calling their notify() method whenever the Switch is activated.

##### Bomb as the Observer
Reacting to Notifications: The Bomb class reacts to notifications from the Switch class.

Update Method: The notify() method in the Bomb class is called by the Switch when it needs to inform the Bomb about an event (activation). This method then triggers the explode() method, which defines the specific response of the Bomb to the notification.


### c) Inheritance Design

https://nw-syd-gitlab.cseunsw.tech/COMP2511/24T2/teams/W15C_CABBAGE/assignment-ii/-/merge_requests/1
https://nw-syd-gitlab.cseunsw.tech/COMP2511/24T2/teams/W15C_CABBAGE/assignment-ii/-/merge_requests/4
(/put/links/here)

> i. Name the code smell present in the above code. Identify all subclasses of Entity which have similar code smells that point towards the same root cause.

The code smell present is Speculative Generality as shown by the unused abstract methods that were inherited from the Entity superclass, which were made in anticipation of future features that might get implemented but never were . This causes unnecessary clutter and makes the code less readable and harder to support.
Subclasses of Entity with similar code smells that point toward the same root cause: 
Exit, Wall, Boulder, Door, Player, Portal, Switch, Enemy, ZombieToastSpawner, Arrow, Bomb, Key, Sword, Treasure, Wood, Buildable, Potion.

> ii. Redesign the inheritance structure to solve the problem, in doing so remove the smells.

I changed the methods onOverlap, onMovedAway and onDestroy from abstract methods to each be a separate interface so that they would only be implemented 
whenever necessary, and not be present in every class that inherits Entity. I also had to modify the methods triggerOverlapEvent, triggerMovingAwayEvent and destroyEntity in the GameMap class so that they would call the refactored methods from the interface. I then went through the codebase and deleted any obsolete instances of the 3 refactored methods.
Refactoring it this way makes the codebase more adherent to the SOLID principles, in particular Interface Segration Principle, as we are now promoting higher cohesion and decoupling.
This way also does not violate the Liskov Substition principle unlike the way I did it previously.

### d) More Code Smells

[https://nw-syd-gitlab.cseunsw.tech/COMP2511/24T2/teams/W15C_CABBAGE/assignment-ii/-/merge_requests/7](/put/links/here)

> i. What design smell is present in the above description?

A change preventer smell called shotgun surgery is what the description sounds like, as it seems that trying to make changes in one place causes code to break in many other classes, meaning further edits are required. This can happen because a responsibility may have been split up amongst many different classes., hence the code violates the Single Responsibility Principle.

> ii. Refactor the code to resolve the smell and underlying problem causing it.

[Briefly explain what you did]
To refactor the code, I made an extra case in the onOverlap method of Player.java to check if the overlapping entitiy implements the InventoryItem interface,
as if it did, it would be a collectable entity. I then parsed the entity through a modified version of the pickUp method, which now upon checking that the 
item has been added to the inventory, will destroy the item from the map. I then went through and refactored all the OnOverlap methods in all the collecatble
entities to call the method in Player.java for item pick ups, hence freeing up the responsibility of picking up items from all the collectable entity classes.

### e) Open-Closed Goals

[https://nw-syd-gitlab.cseunsw.tech/COMP2511/24T2/teams/W15C_CABBAGE/assignment-ii/-/merge_requests/6](/put/links/here)

> i. Do you think the design is of good quality here? Do you think it complies with the open-closed principle? Do you think the design should be changed?

No I do not think the design is of good guality in both Goal.java and GoalFactory.java. This is because of the use of a switch statement in Goal.java, which means that if there was a new goal to be added, the code would have to be modified in Goal.java, violating our principle.

This is not that big of an issue in GoalFactory, as typically in a supposed factory method pattern, the use of a switch statement is necessary. However, we can also see that the initial depiction of Goalfactory is not very well designed as a factory pattern, as it does not create different classes of goals.

Thus yes, I do believe the design should be changed.

> ii. If you think the design is sufficient as it is, justify your decision. If you think the answer is no, pick a suitable Design Pattern that would improve the quality of the code and refactor the code accordingly.

We can fix this issue by implementing a composite pattern.

We first treat our goal as a Component by converting it into an interface instead with its methods achieved() and toString(). 

Then we create classes which implement this interface. We have classes which will act as the composite class such as AndGoal and OrGoal, and Leaf classes such as BoulderGoal and TreasureGoal. We then retrieve the code within the switch statements in the original Goal.java, and override the corresponding methods in their corresponding class. 

In our composite goal classes, we then implement add, remove and getchildren methods.

Now in our GoalFactory class, instead of creating a new Goal(), we instead create a new AndGoal(), etc. depending on which type of goal it is in the switch statement. I have also moved the functionality of creating a goal to their own methods, in order to maintain the Single Responsibility Principle.


### f) Open Refactoring

[https://nw-syd-gitlab.cseunsw.tech/COMP2511/24T2/teams/W15C_CABBAGE/assignment-ii/-/merge_requests/8](/put/links/here)

Refactored the code to eliminate the violations of law of demeter that I found. I changed the onOverlap method in Player.java to not reach into map and then game to call the battle method, instead making a function to handle battles in GameMap. I also refactored how items are removed upon use in Buildable items and also the Sword.java, making a 'Removable' interface to loosen the coupling of the codebase. I changed the final line of the 3 Moving Mercenary Strategies to comply better with the Law of Demeter. 

[https://nw-syd-gitlab.cseunsw.tech/COMP2511/24T2/teams/W15C_CABBAGE/assignment-ii/-/merge_requests/9](/put/links/here)

I fixed the State pattern implementation for the effects of potions, refactoring the triggerNext method in Player.java to adhere to the open closed principle. 
I then went on to refactor Player.java to better adhere to the single responsibility principle, making a class called PotionManager.java, so that Player.java
would not have the responsibility of managing potions anymore. By doing this I encapsulated potion management in a separate class. I had to also make modifications
to ZombieToast.java, BattleFacade.java, Mercenary.java and GameMap.java in order to correctly apply this refactoring change to Player.java. While refactoring these classes, 
I also fixed a few instances of law of demeter violations along the way.

Add all other changes you made in the same format here:

[](/put/links/here)

The buildable entities contained a significant amount of hardcoding, i fixed this by creating more constants in config_template.json and parsing these
constants through EntityFactory.java, which would then be used appropriately in the applyBuffs or any future methods of their respective classes. 


## Task 2) Evolution of Requirements 👽

### a) Microevolution - Enemy Goal

[https://nw-syd-gitlab.cseunsw.tech/COMP2511/24T2/teams/W15C_CABBAGE/assignment-ii/-/merge_requests/15](/put/links/here)
https://nw-syd-gitlab.cseunsw.tech/COMP2511/24T2/teams/W15C_CABBAGE/assignment-ii/-/merge_requests/15 

**Assumptions**
- new goal class: EnemyGoal, which can only hold the two goals below, is also a composite
- new goal classes: DefeatedRequiredGoal and SpawnersDestroyedGoal
- when onDestroyed is run on destroyable enemies, then a variable in game is increased. When that value hits the requirement of the goal, then it is achieved.

**Design**
What fields/methods you will need to add/change in a class
- GoalFactory will be changed
- GoalFactory will have method createDefeatedRequiredGoal() and createSpawnersDestroyedGoal(), createEnemyGoal()
- GoalFactory switch case statement will have to be modified
- Player class will have a getDefeatedEnemiesCount(), and a new attribute private int defeatedEnemiesCount
- This value will be set 0 at the beginning of the game
- Game will also store a value of number of spawners, and player will store a value of numSpawnersDestroyed. When they equal, that goal is achieved

What new classes/packages you will need to create
- DefeatedRequiredGoal class
- SpawnersDestroyedGoal class
- EnemyGoal class
-> in the Goal package and implement Goal interface

**Changes after review**

Kevyn replied to me with Looking Good! and told me there was no issues with the plan I had made. So no changes are needed to be made

**Test list**
1. Testing a map with multiple conjuction goals with enemy goal
2. Testing enemy goal can only store those 2 goals
3. the 2 goal types i.e. enemy defeated and spawners destroyed work
4. no issues with destroying more enemies and spawners required
5. works with an OR goal
6. testing the exit goal must be achieved last in EXIT and ENEMY
7. testing the exit goal must be achieved last in EXIT and (ENEMY and TREASURE etc)

**Other notes**

- fixed law of demeter in weapon usage of player
- destroying spawner wasnt implemented in the codebase, and now is
- game now stores a numSpawners variable
- created new interface EnemySubgoal
- 

### Choice 1 (2D. Sunstone and more Buildables)

[https://nw-syd-gitlab.cseunsw.tech/COMP2511/24T2/teams/W15C_CABBAGE/assignment-ii/-/merge_requests/13](/put/links/here)

**Assumptions**

- The sceptre stays in the players inventory forever, does not get destroyed upon use
- When a player approaches a door with both a key and a sunstone, the sunstone should be used and both should stay in inventory
- A sceptre can mind control multiple mercenaries at once.

**Design**
In your pair blog post, plan out a detailed design for the task. This should include:

What fields/methods you will need to add/change in a class
What new classes/packages you will need to create

Sunstone class in collectables package
    canMoveOnTo and onOverlap works same as the other collectable entities apart from Bomb.
    Extends Entity implements Overlappable, InventoryItem

Sceptre class in Buildables package
    int mindControlDuration field
    int getMindControlDuration method
    applyBuff shouldnt change anything
    use() should not remove the item from the game, should remain in inventory
    extends Buildable abstract class

MidnightArmour class in Biuldables package
    use() does not decrease durability, as theres no midnight armour durability.
    zombieCheck boolean method to check if there are zombies in dungeon
    extends Buildable abstract class implements BattleItem interface

In Inventory.java
    modify checkBuildCriteria method to include new buildables
    modify getBuildables()

In config_template.json 
    add variables for the rest of midnight armours buffs so it wont be harcoded.

In EntityFactory.java
    Update to include new entities.

In Mercenary.java 
    boolean mindControlled()

In Door.java
    hasSunstone() boolean method


**Changes after review**

Roger recommended to me to add the interfaces and classes that the new entities extend so I changed that. 

**Test list**

1. testing sunstone can be picked up and added to inventory
2. testing sunstone can be used to open doors, is retained after use
3. Testing sunstone will count towards treasure goal
4. Testing sunstone will cannot be used to bribe
5. testing sunstone can be used interchangeably with keys and treasure for crafting if insufficient materials
6. Testing sunstone will be retained after crafting if replacing key or treasure
7. Testing sunstone will be consumed if used as listed ingredient
8. Testing player can hold multiple sunstones in inventory

9. test building a sceptre with wood and key
10. test building sceptre with wood and treasure
11. test building sceptre with wood and sunstone
12. test building sceptre with arrow and key
13. test building sceptre with arrow and treasure
14. test building sceptre with arrow and sunstone
15. test sceptre allies enemy
16. test sceptre allies enemy from any distance
17. test enemy is no longer allied on tick 3
18. test correct values of mindControllDuration


19. testing building midnight armour
20. testing midnight armour cannot be crafted with zombies in dungeon
21. testing midnight armour buffs
22. testing midnight armour lasts forever.


**Other notes**

[Any other notes]

### Choice 2 (2f)

[https://nw-syd-gitlab.cseunsw.tech/COMP2511/24T2/teams/W15C_CABBAGE/assignment-ii/-/merge_requests/new?merge_request%5Bsource_branch%5D=roger%2F2f](/put/links/here)

**Assumptions**
- doable
- require a wire class
- require a switchable class to extend to door and bulb
- bubl will have a value that is on/off
- use the isAdjacent method
- wires and switches will have an on/off thing
- all switchables will look at the 4 adjacent positions at each tick
- store the adjacent wires/switches in the lightbulb
- lightbulb depends on logic state and loops through the list of switchables
- at each tick, if a switch is turned off, then we recursively turn off all wires adjacent to that wire. can use a dfs
- strategy pattern for logic


**Design**
##### Used/new Classes
- Lightbulb
- Switch Door
- Switch
- Wire
- Bomb

Switch implements source
-> stores adjacent wires
-> if a switch is turned off, we turn all wires off, and then run dfs from each switch, turning the wires back on again. Then we do switchable logic. This may be inefficient af but there will be a lot of errors otherwise, as wires have no direction.

Wire implements source
-> stores adjacent wires/switch

##### Interfaces
- LogicStrategy
-> contains xor, or, and, co_and strats

- source
-> has on and off value
-> switch, wire
-> just for polymorphism in switchable

- switchable extends entity
-> has on and off value
-> switchdoor, lightbulb, bomb
-> contains list of adjacent sources
----> this is done at the beginning of the game, for each switchable, we check the 4 cardinal positions if there is a wire/switch there
-> if turned on, then only needs 1 source of power
-> check logic strategy, and in strategy class take in the list of sources, and return true/false for if it stays on

logic Strategies
- boolean power(list of sources)
- loop through, return true/false
- co_and needs to be the same tick, so we should store, a previousStatus for each source

**Changes after review**

Kevyn said it was all goods, as I walked him through the entire design on call.

**Test list**

1. test or strat -> tested
2. test and strat -> tested
3. test xor strat -> tested
4. test co_and strat and all its intricacies -> tested
5. test one wire being turned on by 2 switches, and then one switch off, if the wire stays on -> tested
6. test bomb blows up when powered -> tested
7. test switch bulb/door/bomb next to it -> tested
8. test bulb turns on when powered -> tested
9. test door turns on when powered -> tested

**Other notes**

- changed name converter
- wires implement similar observer pattern to bombs like bombs to switch
- turned door into abstract class keydoor and switchdoor
- have to implement what happens if bombs explode a wire
- test normal bombs dont explode to wires and logic bombs do only when placed
- can walk through door when on
- test if  Wires and logical entities will also remain activated as long as there is a current from a switch running through them
- bomb test -> on xor file, we blow up the bottom switch -> tested
- bomb test -> on and file we blow up the wire, and then move all the boulders off and see the the door  doesnt open anymore -> tested
- test door cant be opened with key -> tested
### Choice 3 (Insert choice) (If you have a 3rd member)

[Links to your merge requests](/put/links/here)

**Assumptions**

[Any assumptions made]

**Design**

[Design]

**Changes after review**

[Design review/Changes made]

**Test list**

[Test List]

**Other notes**

[Any other notes]

## Task 3) Investigation Task ⁉️

[Merge Request 1](https://nw-syd-gitlab.cseunsw.tech/COMP2511/24T2/teams/W15C_CABBAGE/assignment-ii/-/merge_requests/new?merge_request%5Bsource_branch%5D=roger%2F3)

How confident are you that your software satisfies the requirements?
We will list out the specifications and how it satisfies the requirements.
1. How confident are you that your software satisfies the requirements?
- players cannot move through walls so yes
2. Player, can be moved up, down, left and right into cardinally adjacent squares
- this is true using wasd, the player moves
- The Player begins the game with a set amount of health and attack damage, this is true.
```
public static final double DEFAULT_ATTACK = 5.0;
    public static final double DEFAULT_HEALTH = 5.0;
```
3. The Player spawns at a set 'entry location' at the beginning of a game. Also true, it based on the position which is given to the constructor.

#### Static Entities
- wall blocks movements of player and boulders: true
```
@Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return entity instanceof Spider;
    }
```
- exit, yes if the goal is only exit, then the player will complete the puzzle if they go through it

- The only difference is that it can be pushed by the Player into cardinally adjacent squares.: true, using the trasnlate method(). 

- The Player is only strong enough to push one boulder at a time.: yes by canPush method, it checks if the entities in the new position can be moved onto.

- Switches behave like empty squares, so other entities can appear on top of them. When a boulder is pushed onto a floor switch, it is triggered. Pushing a boulder off the floor switch untriggers it.
----> true, switches normally dont do anything, but if overlapping a boulder it will operate

- Exists in conjunction with a single key that can open it. If the Player holds the key, they can open the door by moving through it. Once open, it remains open.
----> true, the key and door both have a number which lets this happen.

- Teleports players to a corresponding portal. The player must end up in a square cardinally adjacent to the corresponding portal. The square they teleport onto must also be within movement constraints - e.g. the player cannot teleport and end up on a wall. If all squares cardinally adjacent to the corresponding portal are walls, then the player should remain where they are.
--> Yes, this works, when overalpping with a player it runs doteleport, which rusn moveTo, which checks if the entity can move onto that position

- Spawns zombie toasts in an open square cardinally adjacent to the spawner. The Player can destroy a zombie spawner if they have a weapon and are cardinally adjacent to the spawner. If all the cardinally adjacent cells to the spawner are walls, then the spawner will not spawn any zombies.
----> yes zombies cna only spawn cardinally adjacent to the spawner. 
```
List<Position> pos = spawner.getPosition().getCardinallyAdjacentPositions();
```
---> no players could not destroy a zombie spawner This was implemented in the interact method of the spawner, where map.destroyentity is called when it is interacted with by a player with a weapon.

```
if (player.hasWeapon()) {
            player.useWeapon(game);
            map.destroyEntity(this);
        }
```
#### Enemies 
Spiders spawn at random locations in the dungeon from the beginning of the game. When the spider spawns, they immediately move the 1 square upwards (towards the top of the screen) and then begin 'circling' their spawn spot (see a visual example below).
- yep they spawn at random

```
Position initPosition = availablePos.get(ranGen.nextInt(availablePos.size()));
        Spider spider = buildSpider(initPosition);
```
- they also move 1 square upwards using the 8 positions around it. It sets nexpositionelement to 1.
Spiders are able to traverse through walls, doors, switches, portals, exits (which have no effect), but not boulders, in which case it will reverse direction (see a visual example below).
- yep they can traverse through walls except boulders, as it has its position updated, and when it is a boulder, it changes forward to !forward.
Manhattan radius spawning
- yep 
```
 for (int i = player.getX() - radius; i < player.getX() + radius; i++) {
        for (int j = player.getY() - radius; j < player.getY() + radius; j++) {
```

Zombies

Zombies spawn at zombie spawners and move in random directions. Zombies are limited by the same movement constraints as the Player, except portals have no effect on them.
-> everything was true, except portals had an effect on them in the code of portals. This was fixed by removing it from here
```
if (entity instanceof Player || entity instanceof Mercenary)
    doTeleport(map, entity);
```

Mercenaries
- Mercenaries do not spawn; they are only present if created as part of the dungeon. 
-> true
- They constantly move towards the Player, stopping only if they cannot move any closer (they are able to move around walls). 
-> follow djikstras alogrithm in movement method so yes
- Mercenaries are limited by the same movement constraints as the Player. 
-> canMoveOnto is false for both players and mercs
- All mercenaries are considered hostile, unless the Player can bribe them with a certain amount of gold; in which case they become allies. Mercenaries must be within a certain radius of the player in order to be bribed, which is formed by the diagonally and cardinally adjacent cells in a "square" fashion, akin to the blast radius for bombs. 
- This is wrong. Mercenary bribe radius only checks if the bribe radius is greater than 0, instead of checking if it is in radius of the player
```
private boolean canBeBribed(Player player) {
        Position distance = Position.calculatePositionBetween(player.getPosition(), getPosition());
        int magnitude = distance.magnitude();
        return (bribeRadius >= magnitude && player.countEntityOfType(Treasure.class) >= bribeAmount);
    }
```
--> fixed the code by getting the magnitude between player and merc

As an ally, once it reaches the Player it simply follows the Player around, occupying the square the player was previously in.
- yep uses djikstras again

#### 2.4 collectable entities
- treasure can be collected
- issue with key: player can pick up multiple keys
---> This has been solved, Fixing DoorKeysTest 4-4 to properly check for if only one key can be held at a time, and implementing an extra check in Player.java and Keys.java

old version of pickUp method in Player.java

public void pickUp(InventoryItem item, GameMap map) {
        if (item instanceof Treasure || item instanceof SunStone)
            collectedTreasureCount++;
        boolean added = inventory.add(item);
        if (added) {
            map.destroyEntity((Entity) item);
        }
    }

    public void pickUp(InventoryItem item, GameMap map) {
        // Do not pick up the key if the player already has one
        if (item instanceof Key && inventory.getEntities(Key.class).size() >= 1) {
            return;
        }
        if (item instanceof Treasure || item instanceof SunStone)
            collectedTreasureCount++;
        boolean added = inventory.add(item);
        if (added) {
            map.destroyEntity((Entity) item);
        }
    }

new version with a check to make sure no more than 1 key is in inventory.


- potions work effectively, see movement strategies for invincible and random
- wood and arrow can be picked up
- bomb explodes correctly
- bomb does detonate next to an active switch, or next toa  switch taht becomes active
- The bomb explodes on the same tick it becomes cardinally adjacent to an active switch. A bomb cannot be picked up once it has been used. This is true. Because of the subscribe in switch, which notifies the bomb to flow up with the switch is powered.
- A standard melee weapon. Swords can be collected by the Player and used in battles, increasing the amount of damage they deal by an additive factor. Each sword has a specific durability that dictates the number of battles it can be used before it deteriorates and is no longer usable.
----> yep, each use lowers the durability of the sword, and it also adds damage to the players base damage.

#### 2.5 buildable entities
Some entities can be built using a 'recipe' by the player, where entities are combined to form more complex and useful entities. Once a buildable item has been constructed, it is stored in a player's inventory. For all buildable entities, once the item is constructed the materials used in that construction have been consumed and disappear from the player's inventory.
-> this is correct in Player, build(), the inventory checks if it can build the item, and adds it
-> when building the item in the respective class, the inventory removes the materials

Bow
Can be crafted with 1 wood + 3 arrows. The bow has a durability which deteriorates after a certain number of battles. Bows give the Player double damage in each round, to simulate being able to attack an enemy at range (it can't actually attack an enemy at range).
- double damage done in player battle statistics
- same as shield stats

#### 2.6
A 'round' of a battle occurs as follows:

Player Health = Player Health - (Enemy Attack Damage / 10)
Enemy Health = Enemy Health - (Player Attack Damage / 5)
- yes this happens, as seen in the code, it is applied in battlefacade.java
```
public static final double DEFAULT_PLAYER_DAMAGE_REDUCER = 10.0;
public static final double DEFAULT_ENEMY_DAMAGE_REDUCER = 5.0;
```

- yes the player dies when their health is 0 and enemy too
```
public void battle(Player player, Enemy enemy) {
    battleFacade.battle(this, player, enemy);
    if (player.getBattleStatistics().getHealth() <= 0) {
        map.destroyEntity(player);
    }
    if (enemy.getBattleStatistics().getHealth() <= 0) {
        map.destroyEntity(enemy);
    }
}
```

2.6.1 Weapons in Battle
In battles, weapons and allies provide an attack and defence bonus to the player.
- yes this works, all weapons have a damage attribute

#### 2.7 goals
- this was implemented using a composite pattern
- the goals are achieved when their conditions are met
- complex goals are composite and are achieved when both of their subgoals are achieved
- exit does happen last, this is because in an AND statement with exit, the player must be on the exit to be able to exit, and thus the other things must be done first

### Specification Task 2
#### 2a) 
To achieve the enemy goal, the player must fulfil both of the following requirements:

Defeat at least a certain number of enemies (i.e. ZombieToast, Spider, etc)
Destroy all spawners (i.e. ZombieToastSpawner)

- what I did was make an EnemyGoal and have this goal store a list of subgoals of the interface EnemySubgoal. this was to allow open closed principle, if in the future there were more subgoals int he enemy goal
- enemy goal could onyl sotre unique subgoals as well
- enemy goal would only be achieved if all of its subgoals were achieved
- enemies defeated goal, i had to have player store an amount of enemies defeated, when an eenemy was destroyed. This is done in game.java
- destroy all spawners. Destroying spawners wasnt implemented, this was refered to above. Player also has a stored value of spawners destroyed
- the total number of spawners is counted in gamemap when it is iniating the spawners, and the value is put in game in spawnerCount.
- Enemygoal used the goal interface, so it could also be put in the conjuction complex goals

#### 2f)
Things that were not specified in the spec: what happens to wires when they blow up, lightbulbs being walkable or not
Init all the things in gamemap

Entity factory was used to create wires, switchdoors, ligthbulbs, and logic based bombs
Created the following entity clases
- Lightbulb
- Switch Door
- Switch
- Wire
- LogicBomb

These implemented interfaces based on if they took power or powered others. Switchable and PowerSource
- switchable 
-> has on and off value
-> switchdoor, lightbulb, bomb
-> contains list of adjacent sources
----> this is done at the beginning of the game, for each switchable, we check the 4 cardinal positions if there is a wire/switch there // done in init
-> if turned on, then only needs 1 source of power
-> each tick they are activated and checked if they are now powered/not powered. If so then they change their powered boolean and update their visuals/if they can wlak through/if they blow up.


###### If a switch is activated, it sends a current to any cardinally adjacent wires and logical entities. Current can be conducted through wires and activated switches only (conductors), meaning that a logical entity may be activated directly by a switch if cardinally adjacent, or indirectly by a cardinally adjacent current-carrying wire. This allows for the creation of dungeons with logic circuits.
- Yes switches, stored the adjacent wires and switchables, set up from init in gameMap. If they are turned on, then all adajcent wires, call adjacent wires etc recursively until all connected wires are powered on.

a) Logical Entities
-> these are in the switchable interface
- Bombs which have this attribute will only explode when their logical condition is fulfilled.
-> done through having activate called instead of notify. This means that if it is placed next to the needed power it will instantly blow up. Or if not, it will wait until the wires turn on, and activate is called in tick for all switchables.
- Bombs created without this value will interact with switches in the same way as they do in the MVP
-> yes, bombs and logicbombs are different classes, and are separated in the entityfactory

- Wires and logical entities will also remain activated as long as there is a current from a switch running through them
-> this is done is checkStillOn in wire, and return sources.stream().anyMatch(s -> s.isPowered()) for switchables in checkPoweredNow();
-> checkstillOn checks if the poweredSwitches connected to the wire are still more than 1. And checkPoweredNow() checks if after a player movement, if a the sources can now power the switchable

If the source of power for a logical component has been switched off or destroyed, then the logical component will be deactivated.
- yes the logical entity will turn off, if all sources of power are off after being initially on. This applies for destruction of switches and wires. This was done in the onDestroy method of the two, where it would unsubscribe and turn off adjacent wires and itself, and remove itself from the list<PowerSource> of all switchables.

b) Logical Rules
or, and, xor, co_and
- or, and and xor are simple, basically we made a logicstrategy for all 4 of these logic rules, and created the logical entities with the strategy it was designated. In each strategy it has isOn(), and takes in a list of the adjacent sources. we then check if the sources can power the logical entity based on the strategy

- and does check if theres 2 or more conductors

-co_and was implemented correctly, as all wires/switches have a value justPowered, which is turned on when the conductor is first turned on. It is then switched off the tick after. In the coAnd strategy, it checks if justPowered is true and ispowered is true and then checks if there was more than 2 of the sources like that. thus the coand works correclt.y

Activating the switch on (3, 1) will also activate the bulb's cardinally adjacent wires on (4, 0) and (4, 2) on the same tick, and satisfy the co_and condition. If any or both of the switches on (1, 0) and (1, 2) are activated before the middle switch then the bulb will NOT turn on as the adjacent conductors are activated on different ticks.
- this was done through this:
```
if (!powered) {
    justPowered = true;
}
```
in turnOn();

[Merge Request 2](/put/links/here)

[Briefly explain what you did]

Add all other changes you made in the same format here:
