import { useState } from 'react';
import { getErrorMessage } from '../utils/getErrorMessage';

export default function BookForm({
  initialBook = { title: "", author: "" }, onSave
}) {
const [title, setTitle] = useState(initialBook.title);
const [author, setAuthor] = useState(initialBook.author);
const [saving, setSaving] = useState(false);
const [error, setError] = useState('');
async function save(event) {
  event.preventDefault();
  if (!title.trim() || !author.trim()) {
    setError('Completa título y autor');
    return;
  }
  setSaving(true);
  setError('');
  try {
    await onSave({ title: title.trim(), author: author.trim() });
    setTitle('');
    setAuthor('');
  } catch (error) {
    setError(getErrorMessage(error));
  } finally {
    setSaving(false);
  }
}
return (
  <form onSubmit={save} className="mb-4">
    {error && <p role="alert" className="alert alert-danger">{error}</p>}
    <label htmlFor="title">Título</label>
    <input id="title" className="form-control mb-2" required
      value={title} onChange={e => setTitle(e.target.value)} />
    <label htmlFor="author">Autor</label>
    <input id="author" className="form-control mb-2" required
      value={author} onChange={e => setAuthor(e.target.value)} />
    <button className="btn btn-primary" disabled={saving}>
      {saving ? 'Guardando…' : 'Guardar libro'}
    </button>
  </form>
);
}
