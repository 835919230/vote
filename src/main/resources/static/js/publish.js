/**
 * Created by hexi on 16-10-7.
 */
function deleteChoice() {

}
var oBox = document.getElementById('choice-area');
var sChoiceInput = "<div class='choice'> " +
                        "<input type='text' name='choice' class='choice' placeholder='请输入选项名称' value='choiceName'/>" +
                    "</div>";
var oAddChoice = document.getElementById('addChoice');
oAddChoice.onclick = function (event) {
    // event.preventBubble();
    // oBox.text = oBox.text + sChoiceInput;
    // alert('2222')
    // console.log(oBox.innerHTML)
    oBox.innerHTML = oBox.innerHTML + sChoiceInput;
}