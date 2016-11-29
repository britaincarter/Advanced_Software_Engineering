/**
 * 
 */

var selAttrChanged = function(sel1, sel2) {
	if(sel1.selectedIndex != sel2.selectedIndex) {
		sel2.selectedIndex = sel1.selectedIndex;
		
		var selected = $('#selAttr option:selected');
		$('#selUserAttr option').each(function(e) {
			if($(selected).attr('value') == $(this).attr('value')) {
				$(this).prop('selected', true);
				$('#txtVal').val($('#selVal option:selected').val());
			}
		});
	}
}

$(function() {
	var selAttr = document.getElementById("selAttr");
	var selVal = document.getElementById("selVal");
	
	$(selAttr).change(function() { selAttrChanged(selAttr, selVal); });
	$(selVal).change(function() { selAttrChanged(selVal, selAttr); });
	
	$('#btnAddStyle').click(function(e) {
		
		$('#selUserAttr option:selected').each(function() {
			var selected = this;
			var idx = 0;
			var appended = false;
			$('#selAttr option').each(function() {
				if($(this).attr('value') == $(selected).attr('value')) {
					$(selVal.options[idx]).html($('#txtVal').val());
					appended = true;
				}
				else {
					idx++;
				}
			});
			
			if(!appended) {
				var newAttr = $(selected).clone();
				$(selAttr).append(newAttr);
				
				var newVal = document.createElement("option");
				$(newVal).html($('#txtVal').val());
				$(selVal).append(newVal);
			}
			
			$('#msgTxt').css($(selected).html(), $('#txtVal').val());
		});
		
		$('#txtVal').val('');
		$('#selUserAttr').focus();
		
		
	});
	
	$('#btnRemStyle').click(function(e) {
		
		var idx = selAttr.selectedIndex;
		
		var attr = $(selAttr.options[idx]).html();
		$('#msgTxt').css(attr, '');
		
		$(selAttr.options[idx]).remove();
		$(selVal.options[idx]).remove();
		
	});
	
});