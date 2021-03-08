$(document).ready(function () {
    let requestId = 0;

    $(document).on("click", "#deleteModalOpen", function () {
        requestId = $(this).data('id');
        $('#deleteModal .modal-body')
            .text("Are you sure you want to delete this request?");
        // .text("Do you want delete a request with id = " + requestId + " ?");
        $('#deleteModal').modal('show');
    });

    $(document).on("click", "#deleteBtn", function () {
        $.ajax({
            url: '/api/requests/' + requestId,
            type: 'DELETE',
            success: function (response) {

                $('#' + requestId).fadeOut(1000).css({background: 'pink'});
                $('#deleteModal').modal('hide');
                // alert('Policy Request has been deleted');
            },
            error: function (xhr, status, error) {

                console.log(error);
                var errorMessage = xhr.status + ': ' + xhr.statusText
                alert('Error - ' + errorMessage);
            }
        });
    });
    $('#collapseBtn').click(function () {
        $('#collapseOne').slideToggle('slow');
    });
    $('#policiesBtn').click(function () {
        if ($(this).data('status') === 'show') {
            $(this).text("Show Policy Requests");
            $(this).data('status', 'hide');
        } else {
            $(this).text("Hide Policy Requests");
            $(this).data('status', 'show');
        }
        $('.policies').toggle();
    });

    $('#quotesBtn').click(function () {
        if ($(this).data('status') === 'show') {
            $(this).text("Show Quote Requests");
            $(this).data('status', 'hide');
        } else {
            $(this).text("Hide Quote Requests");
            $(this).data('status', 'show');
        }
        $('.quotes').toggle();
    });
});