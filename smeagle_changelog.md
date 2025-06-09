# Nowlkyrie Changes

New Mixin's:
* AxisAlignedBBMixin
  * ~1.21x faster, uses FastMath.min/max
* IntegerCacheMixin 
  * ~2.98x faster, uses Integer.valueOf directly
* ResourceLocationMixin
  * ~1.77x faster, uses a better splitObjectName method 
* RenderHelperMixin
  * Caches FloatBuffers to avoid allocations and improve CPU usage

Mixin Changes:
* MathHelperMixin 
  * Uses a lookup table for sin and cos like vanilla, except vastly smaller
  * This reduces accuracy a lot from what was already a pretty inaccurate method, but it's generally more performant 

Other Changes:
* Split debug's `enabled` config into a `debugScreenEnabled` config and defaulted both to false
* Added a way to offset the extra layers using the new `layerOffset` config
* Added support for generating a cloud texture at runtime, (WIP not recommended)
* Ported the config `javaVersionCheck` so you can turn it off if you need