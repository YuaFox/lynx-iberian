import {LitElement} from 'lit'

export class LynxView extends LitElement {

    name : string = ""

    constructor(name: string) {
        super()
        this.name = name
    }
}
