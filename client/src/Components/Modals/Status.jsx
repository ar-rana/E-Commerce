import React from "react";
import Modal from "react-modal";

const Status = (props) => {
  return (
    <>
      <Modal
        style={{
          content: {
            top: "7rem",
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
            <tr>
              <th>Item</th>
              <th>Details</th>
            </tr>
            <tr>
              <td>Order Id</td>
              <td>{props.order?.orderId}</td>
            </tr>
            <tr>
              <td>User</td>
              <td>{props.order?.userIdentifier}</td>
            </tr>
            <tr>
              <td>Contect</td>
              <td>{props.order?.phone}</td>
            </tr>
            <tr>
              <td>Address</td>
              <td>{props.order?.address}</td>
            </tr>
            <tr>
              <td>Product Name</td>
              <td>{props.order?.product.name}</td>
            </tr>
            <tr>
              <td>Product Current Price</td>
              <td>{props.order?.product.currentPrice}</td>
            </tr>
            <tr>
              <td>Status</td>
              <td>{props.order?.status}</td>
            </tr>
          </table>
        </div>
      </Modal>
    </>
  );
};

export default Status;
