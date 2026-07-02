# Changelog

All notable changes to this project will be documented in this file.

## Version 3.1.0
### Changed
- Internal Refactoring to potentially improve performance for NBT Leveling

### Added
- NBT Support for single item shorthand `leveling_type`
- Adding Attributes to weapons that don't have them by default by filling out the parameters `addIfNonExistent` and `slotsForNonExistent`/`slotForNonExistent` in the `attributes` section
- new `getLevelableItem` method to `LevelingAPI`
- 2 new leveling types: `weaponleveling:passive_hand` for the offhand (or main if the offhand is used as the attack hand) and `weaponleveling:passive_inv` for all items in the inventory besides the attack item (excludes offhand and armor) 
- debug command `/print_tag_list` to print the tag lists of an item or entity

### Fixed
- Entity blacklist not applying
- `weaponleveling:broken` being able to get registered as a levelable item

## Version 3.0.1
### Added
- New tags under `weaponleveling` namespace (namely `helmets`,`chestplates`,`leggings`,`boots`,`swords`,`axes`)

### Changed
- Improved compatibility with all sorts of default tags in `weaponleveling` tags
- Changed `item` property for leveling files of `helmets`,`chestplates`,`leggings`,`boots`,`swords` and `axes`

### Fixed
- Tooltips not showing for other players on local servers due to a missing sync
- More Syncing issues


## Version 3.0.0
### Added
- Attribute Leveling where you can level any and all attributes an Item has
- Leveling Functions (Math Functions)
- Leveling Types (Worn, Melee, Ranged, None)
- Leveling API

### Changed
- Datapack System has been improved

### Removed
- builtin Mod Support in Favour of Addons
