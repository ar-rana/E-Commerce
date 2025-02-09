import React from "react";
import Modal from "react-modal";

const Status = (props) => {
  return (
    <>
      <Modal
        style={{
          content: {
            top: "5rem",
          },
        }}
        className="modal"
        isOpen={props.open}
        onRequestClose={() => props.setOpen(false)}
        ariaHideApp={false} // avoid warnings
      >
        <div className="modal-container">
          <div className="">
            <div className="modal-close">
              <p>Status: {props.order?.status}</p>
              <span
                className="fa fa-times"
                onClick={() => props.setOpen(false)}
              ></span>
            </div>
          </div>
          <table className="modal-table">
            <thead>
              <tr>
                <th>Item</th>
                <th>Details</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>Order Id</td>
                <td>{props.order?.orderId}</td>
              </tr>
              <tr>
                <td>Name</td>
                <td>{props.order?.customer}</td>
              </tr>
              <tr>
                <td>User</td>
                <td>{props.order?.userIdentifier}</td>
              </tr>
              <tr>
                <td>Reference Id.</td>
                <td>{props.order?.referenceNumber}</td>
              </tr>
              <tr>
                <td>Contact</td>
                <td>{props.order?.contact}</td>
              </tr>
              <tr>
                <td>Address</td>
                <td>{props.order?.address}</td>
              </tr>
              <tr>
                <td>Product Name</td>
                <td>{props.order?.product?.name}</td>
              </tr>
              <tr>
                <td>Product Current Price</td>
                <td>{props.order?.product?.currentPrice}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </Modal>
    </>
  );
};

export default Status;
