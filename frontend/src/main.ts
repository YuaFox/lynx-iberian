export { LynxNav } from "./view/lynx-nav"
export { LynxSidenav } from "./view/lynx-sidenav"
export { LynxViewBuckets } from './view/lynx-view-buckets'
export { LynxContainerMain } from './view/lynx-container-main'

import { LynxSidenav } from "./view/lynx-sidenav"
import { LynxViewBuckets } from "./view/lynx-view-buckets"
import { LynxViewPublishers } from "./view/lynx-view-publishers"

LynxSidenav.elements.push(new LynxViewBuckets())
LynxSidenav.elements.push(new LynxViewPublishers())