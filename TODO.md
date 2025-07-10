### General
- [ ] Update everything!

### Block Entities
- [ ] AdvancedBrewingStandBlockEntity
  - Consolidate to common
    - Neoforge considerations
      - Need to fire events for attempt brew and after brew
      - Register InvWrapper/SidedInvWrapper capability
- [ ] PotionInjectorBlockEntity
  - Consolidate to common
    - Neoforge considerations
      - Register ComponentItemHandler capability
    - Fabric considerations
      - Register fallback ContainerComponentStorage on item
  - Use DataComponents.CONTAINER
- [ ] ReinforcedCauldronBlockEntity
  - Consolidate to common (relatively painless)

### Client
- [ ] Update Gauntlet item models (Investigate if we can consolidate to common)
- [ ] GeoItemModel seems duplicated?

### Umbrella
- [ ] Make Umbrella only bounce potions, not all attacks (New change)
- [ ] Consolidate renderer

### Networking
- [ ] Investigate if we can consolidate net code

### Platform
- [ ] Remove platform service provider
  - If needed, use @Expect/@Actual or @ClassExtension from cloche