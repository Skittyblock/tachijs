package uy.kohesive.injekt

import uy.kohesive.injekt.api.InjektScope
import uy.kohesive.injekt.registry.default.DefaultRegistrar

var Injekt: InjektScope = InjektScope(DefaultRegistrar())
