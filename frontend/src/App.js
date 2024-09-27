<template>
  <div>
    <div
    @dragover.prevent
    @drop.prevent="onDrop"
    class="drop-zone"
    :class="{'drag-over': isDragOver}"
    @dragenter.prevent="isDragOver = true"
    @dragleave.prevent="isDragOver = false"
    >
    <p>Drag and drop your CSV file here, or click to select</p>
    <input type="file" @change="onFileSelected" ref="fileInput" style="display:
    none" accept=".csv" />
  </div>
  <button
  @click="uploadFile" :disabled="!selectedFile">Upload</button>
</div>
</template>

<script>
  import axios from 'axios';

  export default {
  data() {
  return {
  isDragOver: false,
  selectedFile: null,
};
},
  methods: {
  onDrop(event) {
  this.isDragOver = false;
  this.selectedFile = event.dataTransfer.files[0];
},
  onFileSelected(event) {
  this.selectedFile = event.target.files[0];
},
  uploadFile() {
  if (!this.selectedFile) return;

  const formData = new FormData();
  formData.append('file', this.selectedFile);

  axios.post('/api/csv/import/1', formData, {
  headers: {
  'Content-Type': 'multipart/form-data'
}
})
  .then(response => {
  console.log('File uploaded successfully', response);
  // Handle success (e.g., show a success message)
})
  .catch(error => {
  console.error('Error uploading file', error);
  // Handle error (e.g., show an error message)
});
}
}
};
</script>

<style scoped>
  .drop-zone {
  border: 2px dashed #ccc;
  border-radius: 20px;
  width: 480px;
  margin: 50px auto;
  padding: 20px;
}
  .drag-over {
    border - color: #000;
  background-color: #f0f0f0;
}
</style>