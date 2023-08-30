import { useState } from "react";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router";
import { addAuthorizeTransaction } from "../../modules/transactions";

const TransactionAuthorizeForm = () => {

  const [author, setAuthor] = useState('');
  const [title, setTitle] = useState('');

  const dispatch = useDispatch();
  const navigate = useNavigate();

  const handleSubmit = (event) => {
    event.preventDefault();
    if (!author || !title) {
      return;
    }
    dispatch(addAuthorizeTransaction({ author, title }))
      .then(() => navigate("/"))
  };

  return (
    <div className="row">
      <div className="col-sm-6">
        <form onSubmit={handleSubmit}>
          <h1>Add a new transaction:</h1>
          <div className="mb-3">
            <label htmlFor="author" className="form-label">Author</label>
            <input type="text" className="form-control" placeholder="Author"
                   value={author} onChange={(e) => setAuthor(e.target.value)}/>
          </div>
          <div className="mb-3">
            <label htmlFor="title" className="form-label">Title</label>
            <input type="text" className="form-control" placeholder="Title"
                   value={title} onChange={(e) => setTitle(e.target.value)}/>
          </div>
          <button type="submit" className="btn btn-primary">Add transaction</button>
        </form>
      </div>
    </div>
  );
}

export default TransactionAuthorizeForm
