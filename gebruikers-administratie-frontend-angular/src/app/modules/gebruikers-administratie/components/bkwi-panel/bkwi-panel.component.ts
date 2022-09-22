import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-bkwi-panel',
  templateUrl: './bkwi-panel.component.html',
  styleUrls: ['./bkwi-panel.component.scss']
})
export class BkwiPanelComponent {

  @Input() id : string = "";
  @Input() panelTitle : string = "";
  @Input() extraClassesForPanel : string = "";
  @Input() extraClassesForPanelBody : string = "";

}
