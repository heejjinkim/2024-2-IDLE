import { useParams } from "react-router-dom"

export default function StationPage() {
    const {id} = useParams();
    return (
        <div>
            <h1>StationPage {id}</h1>
        </div>
    )
}