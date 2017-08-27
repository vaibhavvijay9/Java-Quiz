function check()
{
	var marked=document.querySelector('input[name="option"]:checked').value;;
	
	if(marked==null)
	{
		alert("Please select an option.");
		return false;
	}
}