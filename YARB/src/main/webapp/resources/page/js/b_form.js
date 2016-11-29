/**
 * 
 */

var userSelected = function(e) {
	console.log("User selected");
	$('#selUsers option:selected').each(function() {
		console.log("Option each()");
		var perm = perms[$(this).attr('value')];
		$('#txtUser').val($(this).text());
		
		$('#chkCanRead').prop('checked', perm['can_read']);
		$('#chkCanWrite').prop('checked', perm['can_write']);
		$('#chkCanInvite').prop('checked', perm['invite_users']);
		$('#chkAdmin').prop('checked', perm['administer']);
		
		$('#btnAddUser').attr({
			'class': 'btn btn-danger',
			'value': userop_rem
		});
		$('#btnAddUser').html('Remove User');
		
		$('#btnModUser').attr('style', 'display: inline;');
	});
}

var userTxtChanged = function(e) {
	console.log("User text changed");
	
	$('#selUsers option').prop('selected', false);
	$('#btnAddUser').attr({
		'class': 'btn btn-success',
		'value': userop_add
	});
	$('#btnAddUser').html('Add User');
	
	$('#btnModUser').attr('style', 'display: none;');
}

$(function() {
	if(user_perms.administer) {
		$('#selUsers').change(userSelected);
		$('#txtUser').on('input', userTxtChanged);
	}
});