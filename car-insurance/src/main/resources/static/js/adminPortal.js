$(document).ready(function () {
    let requestId = 0;

    const statusRejectedHtml =
        '<h4>\n' +
        '<span class="badge badge-pill badge-danger">Rejected</span>\n' +
        '</h4>';
    const statusApprovedHtml =
        '<h4>\n' +
        '<span class="badge badge-pill badge-success">Approved</span>\n' +
        '</h4>';

    function renderPolicyInfo(policyDto) {
        let htmlResult =
            '<ul class="list-group">' +
            '<li class="list-group-item"><b>User : </b>' + policyDto.userFullName + '</li>' +
            '<li class="list-group-item"><b>Vechicle : </b>' + policyDto.vehicleMake + ' ' + policyDto.vehicleModel + '</li>' +
            '<li class="list-group-item"><b>Cubic Capacity : </b>' + policyDto.cubicCapacity + '</li>' +
            '<li class="list-group-item"><b>Driver Age : </b>' + policyDto.driverAge + '</li>' +
            '<li class="list-group-item"><b>Previous Accidents : </b>' + (policyDto.prevAccidents ? 'yes' : 'no') + '</li>' +
            '<li class="list-group-item"><b>Total Amount : </b>' + policyDto.totalAmount + '</li>' +
            '<li class="list-group-item"><b>Request Date : </b>' + policyDto.requestDate + '</li>' +
            '<li class="list-group-item"><b>Effective Date : </b>' + policyDto.effectiveDate + '</li>' +
            '<li class="list-group-item">Vehicle registration certificate :</li>' +
            '<li class="list-group-item">' +
            ' <a href="' + policyDto.imageUrl + '" target="_blank">\n' +
            '   <img style="width:100%" src="' + policyDto.imageUrl + '" />\n' +
            ' </a></li>' +
            '</ul>';

        return htmlResult;
    }
    function changeStatus(newStatus) {

        console.log(newStatus);
        $.ajax({
            url: '/api/requests/change-status/' + requestId,
            type: 'POST',
            data: newStatus,
            // dataType : 'json',
            success: function () {
                let tdToUpdate = '#' + requestId + ' #status';
                let htmlChange;
                switch (newStatus.toLowerCase()) {
                    case 'rejected':
                        htmlChange = statusRejectedHtml;
                        break;
                    case 'approved':
                        htmlChange = statusApprovedHtml;
                        break;
                }
                $(tdToUpdate).html(htmlChange);
            },
            error: function (xhr, status, error) {
                let errorMessage = xhr.status + ': ' + xhr.statusText
                alert('Error - ' + errorMessage);
            }
        });
    }

    $(document).on("click", "#detailsModalOpenBtn", function () {

        requestId = $(this).data('request-id');
        $('#detailsModal').modal('show');

        $.ajax({
            url: '/api/requests/' + requestId,
            type: 'GET',
            success: function (policyDto) {
                $('#detailsModal .modal-body').html(renderPolicyInfo(policyDto));
            },
            error: function (xhr, status, error) {
                let errorMessage = xhr.status + ': ' + xhr.statusText
                alert('Error - ' + errorMessage);
            }
        });
        // the buttons when you open the details modal
        $('#rejectedBtn').on("click", function () {
            changeStatus('Rejected');
        });
        $('#approvedBtn').on("click", function () {
            changeStatus('Approved');
        });
    });
});
